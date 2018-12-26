package com.clubank.op;

import java.util.Vector;


import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class Login extends OPBase {

    public Login(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("userId", args[0]);
        row.put("password", args[1]);
        row.put("authentication", args[2]);
        row.put("machineName", args[3]);
        row.put("clientVersion", args[4]);
        Object obj = operate("Login", row);
        Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
        if (obj != null) {
            if (obj instanceof SoapPrimitive) {
                result.code = Integer.parseInt(obj.toString());
            } else if (obj instanceof Vector) {
                Vector<?> v = (Vector<?>) obj;
                result.code = Integer.parseInt(v.get(0).toString());
                SoapObject so = (SoapObject) v.get(1);
                result.obj = getRow(so);
            }
        }
        return result;
    }

}
