package com.zxing.android.common.executor;

import com.zxing.android.common.PlatformSupportManager;

public final class AsyncTaskExecManager extends
		PlatformSupportManager<AsyncTaskExecInterface> {

	public AsyncTaskExecManager() {
		super(AsyncTaskExecInterface.class, new DefaultAsyncTaskExecInterface());
		addImplementationClass(
				11,
				"com.google.zxing.client.android.common.executor.HoneycombAsyncTaskExecInterface");
	}

}
