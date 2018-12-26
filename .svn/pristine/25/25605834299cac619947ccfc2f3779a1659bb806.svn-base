package com.clubank.op;

import java.util.Vector;


import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class AddBillLock extends OPBase {

	public AddBillLock(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("token", args[0]);
		row.put("billcode", args[1]);
		row.put("lockUser", args[2]);
		row.put("lockDesc", args[3]);
		row.put("lockClientIP", args[4]);
		Object obj = operate("AddBillLock", row);
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
