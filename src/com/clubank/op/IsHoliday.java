package com.clubank.op;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.Vector;

/**
 * Created by long on 17-12-25.
 */

public class IsHoliday extends OPBase {
    public IsHoliday(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("datime", args[0]);
        Object obj = operate("IsHoliday", row);
        Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
        if (obj != null) {
            if (obj instanceof SoapPrimitive) {
                result.code = RT.SUCCESS;
                result.obj = Integer.parseInt(obj.toString());
            }
        }
        return result;
    }
}
