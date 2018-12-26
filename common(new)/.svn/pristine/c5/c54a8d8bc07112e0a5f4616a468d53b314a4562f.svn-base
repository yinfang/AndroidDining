package com.clubank.device.op;

import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class UpdateLocation extends OPBase {

	public UpdateLocation(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
		MyRow row = new MyRow();
		row.put("udid", args[0]);
		row.put("longitude", args[1]);
		row.put("latitude", args[2]);
		Object obj = operate("UpdateLocation", row);
		Result result = new Result(RT.UNKNOWN_ERROR);
		if (obj != null) {
			SoapPrimitive s = (SoapPrimitive) obj;
			result.code = Integer.parseInt(s.toString());
		}
		return result;
	}

}
