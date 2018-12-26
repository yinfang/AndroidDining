package com.clubank.device.op;

import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class SignDevice extends OPBase {

	public SignDevice(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
		Result result = new Result(RT.UNKNOWN_ERROR);
		MyRow row = new MyRow();
		row.put("info", args[0]);
		Object obj = operate("SignDevice", row);
		if (obj != null) {
			if (obj instanceof SoapPrimitive) {
				SoapPrimitive s = (SoapPrimitive) obj;
				result.code = Integer.parseInt(s.toString());
			}
		}
		return result;
	}
}
