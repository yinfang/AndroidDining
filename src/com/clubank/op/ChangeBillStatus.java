package com.clubank.op;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.Vector;

/**
 * Created by long on 17-8-29.
 */

public class ChangeBillStatus extends OPBase {
    public ChangeBillStatus(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("token", args[0]);
        row.put("billCode", args[1]);
        Object obj = operate("ChangeBillStatus", row);
        Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
        if (obj != null) {
            if (obj instanceof SoapPrimitive) {
                result.code = Integer.parseInt(obj.toString());
            } else if (obj instanceof Vector) {
                Vector<?> v = (Vector<?>) obj;
                result.code = Integer.parseInt(v.get(0).toString());
                result.obj = v.get(1).toString();
            }
        }
        return result;
    }
}
