package com.clubank.device.op;

import java.util.Vector;

import android.content.Context;

import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class GetUpPayString extends OPBase {

	public GetUpPayString(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
		MyRow row = new MyRow();
		row.put("orderNo", args[1]);
		row.put("useBalance", args[2]);
		row.put("type", args[3]);
		Object obj = operate("GetUpPayString", row);
		Result result = new Result(RT.UNKNOWN_ERROR);
		if (obj != null) {
			Vector<?> s = (Vector<?>) obj;
			result.code = Integer.parseInt(s.get(0).toString());
			result.obj = s.get(1).toString();
		}
		return result;
	}

}
