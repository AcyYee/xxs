package com.cwy.xxs.util;

import com.cwy.xxs.dvo.AbstractWxPayDo;
import com.cwy.xxs.entity.OrderInfo;
import com.cwy.xxs.util.wxpaysdk.WXPay;
import com.cwy.xxs.util.wxpaysdk.WXPayConfigImpl;
import com.cwy.xxs.util.wxpaysdk.WXPayConstants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.cwy.xxs.util.wxpaysdk.WXPayUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.cwy.xxs.config.PayOrderConfig.ORDER_COM_REFUSE;
import static com.cwy.xxs.config.PayOrderConfig.PAY_NOTIFY;
import static com.cwy.xxs.config.PayOrderConfig.REFUND_NOTIFY;
import static com.cwy.xxs.util.wxpaysdk.WXPayConstants.HMACSHA256;
import static com.cwy.xxs.util.wxpaysdk.WXPayConstants.SUCCESS;

/**
 * @author onepieces
 */
public class WXpaySDKUtil {

    private static WXPayConfigImpl config;

    static {
        try {
            config = WXPayConfigImpl.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WXPay wxpay;

    static {
        try {
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Logger logger = LoggerFactory.getLogger(WXpaySDKUtil.class);

    public static Map<String,String> payRequestSmallProgram(String body, String orderPrice,
    		String openid, String ip,String orderId) {

        Map<String, String> params = new HashMap<>(21);
        params.put("body", body);
        params.put("out_trade_no", orderId);
        params.put("device_info", "WEB");
        params.put("total_fee", orderPrice);
        params.put("spbill_create_ip", ip);
        params.put("notify_url", PAY_NOTIFY);
        params.put("trade_type","JSAPI");
        params.put("openid", openid);

        Map<String, String> map;
        try {
            map = wxpay.unifiedOrder(params);
            logger.info(map.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //返回状态码
        String returnCode = map.get("return_code");
        //返回信息

        if(returnCode!= null && SUCCESS.equals(returnCode)){
            // 业务结果
            //返回的预付单信息
            String prepayId = map.get("prepay_id");
            Map<String,String> paySms = new HashMap<>(5);
            paySms.put("package","prepay_id="+prepayId);

            paySms.put("appId",config.getAppID());
            Long timeStamp= System.currentTimeMillis()/1000;
            paySms.put("timeStamp", String.valueOf(timeStamp));
            paySms.put("nonceStr", WXPayUtil.generateUUID());
            paySms.put("signType", HMACSHA256);
            try {
                paySms.put("paySign",WXPayUtil.generateSignature(paySms,config.getKey()));
                paySms.put("prepay_id",prepayId);
                logger.info(params.toString());
                return paySms;
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        String returnMsg = map.get("return_msg");
        logger.error(returnMsg);
        return null;
    }

	public static String setXML(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code>"
        		+ "<return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
    }

    public static boolean isPayResultNotifySignatureValid(Map<String, String> map) {
        try {
            return wxpay.isPayResultNotifySignatureValid(map);
        } catch (Exception e) {
            logger.error("检验签名出错"+e.toString());
            return false;
        }
    }

    public static String takeWxNotifyBack(Map<String,String> map ,AbstractWxPayDo payDo){
        String returnCode = map.get("return_code");
        if (returnCode != null && returnCode.equals(SUCCESS)) {
            if (WXpaySDKUtil.isPayResultNotifySignatureValid(map)) {
                return payDo.run();
            } else {
                return WXpaySDKUtil.setXML("FAIL", "签名错误");
            }
        } else {
            String returnMsg = map.get("return_msg");
            return WXpaySDKUtil.setXML("FAIL", returnMsg);
        }
    }

    public static boolean refundOrder(OrderInfo orderInfo) {

        Map<String, String> params = new HashMap<>(21);
        params.put("out_trade_no",orderInfo.getOrderId());
        params.put("out_refund_no","refund"+orderInfo.getOrderId());
        params.put("notify_url",REFUND_NOTIFY);
        params.put("total_fee",String.valueOf(Math.round(orderInfo.getAllowPrice() * 100)));
        params.put("refund_fee",String.valueOf(Math.round(orderInfo.getPayPrice() * 100)));
        Map<String, String> map;
        try {
            map = wxpay.refund(params);
            logger.info(map.toString());
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
        //返回状态码
        String returnCode = map.get("return_code");
        //返回信息
        if(returnCode!= null && SUCCESS.equals(returnCode)){
            return true;
        }else {
            String returnMsg = map.get("return_msg");
            logger.error("退款失败"+returnMsg);
            return false;
        }
    }

    public static String decryptData(String reqInfo) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            String b = new String(decoder.decode(reqInfo),"UTF-8");
            byte[] key = WXPayUtil.md5(config.getKey()).toLowerCase().getBytes();
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(decoder.decode(b)));
        } catch (Exception e) {
            e.printStackTrace();
            return "解密失败";
        }
    }
}
