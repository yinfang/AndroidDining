package com.clubank.op;

import java.util.Vector;


import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class GetDishImage extends OPBase {

	public GetDishImage(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("code", args[0]);
		if (args[1] != null) {
		    row.put("time", args[1]);
		}
		Object obj = operate("GetDishImage", row);
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
