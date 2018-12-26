package com.clubank.op;

import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class DeliverDish extends OPBase {

	public DeliverDish(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("saleId", args[0]);
		Object obj = operate("DeliverDish", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = Integer.parseInt(obj.toString());
		    }
		}
		return result;
	    }
}
