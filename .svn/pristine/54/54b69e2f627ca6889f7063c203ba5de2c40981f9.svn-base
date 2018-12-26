package com.clubank.op;

import org.ksoap2.serialization.SoapPrimitive;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import android.content.Context;
/*
 *   检查沽清情况
 *   <returns>0：已沽清或剩余数量不够  1：可正常销售</returns>
 * 
 */
public class CheckSoldOut extends OPBase{

	public CheckSoldOut(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("soldDate", args[0]);   //开单日期
		row.put("itemCode", args[1]); //商品编码
		row.put("soldQuantity", args[2]);//菜品数量
		Object obj = operate("CheckSoldOut", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = Integer.parseInt(obj.toString());
		    }
		}
		return result;
	    }
}
