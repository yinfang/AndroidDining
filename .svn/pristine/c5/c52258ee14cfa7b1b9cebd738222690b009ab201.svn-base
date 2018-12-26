package com.clubank.device;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.Vector;

/**
 * 根据姓名查询消费卡号
 * Created by duxy on 2018/2/6.
 */

public class QueryCheckinInfoByCard extends OPBase {
    public QueryCheckinInfoByCard(Context context) {
        super(context);
    }
    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("criteria", args[0]);
        Object obj = operate("QueryCheckinInfoByCard", row);
        Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
        if (obj != null) {
            if (obj instanceof SoapPrimitive) {
                if(Integer.parseInt(obj.toString())==-1){
                    result.code =  0;
                }else{
                    result.code = Integer.parseInt(obj.toString());
                }

            }else if(obj instanceof Vector<?>){
                Vector<?> v = (Vector<?>) obj;
                if(Integer.parseInt(v.get(0).toString())==-1){
                    result.code =  0;
                }else{
                    result.code = Integer.parseInt(v.get(0).toString());
                }
                SoapObject so = (SoapObject) v.get(1);
                result.obj = getList(so);
            }
        }
        return result;
    }
}
