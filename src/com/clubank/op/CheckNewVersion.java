package com.clubank.op;

import java.util.Vector;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class CheckNewVersion extends OPBase {

	public CheckNewVersion(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("currVersion", args[0]);
		// row.put("type", args[1]);
		Object obj = operate("CheckNewVersion", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    Vector<?> s = (Vector<?>) obj;
		    result.code = Integer.parseInt(s.get(0).toString());
		    SoapObject so = (SoapObject) s.get(1);
		    MyRow row1 = getRow(so);
		    row1.put("manual", args[1]);
		    result.obj = row1;
		}
		return result;
	    }
}
