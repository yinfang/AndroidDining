package com.clubank.op;

import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;
/**
 *记账
 * @author fenyq
 *
 */
public class ChargeOrder extends OPBase {

	public ChargeOrder(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("billCode", args[0]);
		row.put("cardNo", args[1]);
		row.put("token", args[2]);
		Object obj = operate("ChargeOrder", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = Integer.parseInt(obj.toString());
		    }
		}
		return result;
	    }
}
