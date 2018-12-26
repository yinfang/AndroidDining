package com.clubank.device.op;

import java.util.Vector;



import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;

import com.clubank.domain.RT;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class GetDiningPrinterSH extends OPBase {

	public GetDiningPrinterSH(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		Object obj = operate("GetDiningPrinter", row);
		Result result = new Result(RT.UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = Integer.parseInt(obj.toString());
		    } else if (obj instanceof Vector) {
			Vector<?> v = (Vector<?>) obj;
			result.code = Integer.parseInt(v.get(0).toString());
			SoapObject so = (SoapObject) v.get(1);
			result.obj = getList(so);
		    }
		}
		return result;
	    }
}
