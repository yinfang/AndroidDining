package com.clubank.op;

import java.util.Vector;
import org.ksoap2.serialization.SoapPrimitive;
import android.annotation.SuppressLint;
import android.content.Context;
import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class GetSysVersion extends OPBase {

	public GetSysVersion(Context context) {
		super(context);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		Object obj = operate("GetSysVersion", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = 0;
			result.obj = obj.toString();
		    } else if (obj instanceof Vector) {
			result.code = 0;
			result.obj = obj.toString();
		    }
		}
		return result;
	}

}
