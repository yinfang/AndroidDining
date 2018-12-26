
package com.clubank.util;

import android.support.v4.widget.SwipeRefreshLayout;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

public class RxNetworking {

    public static <T> Observable.Transformer<T, T> bindRefreshing(final SwipeRefreshLayout srl) {
        return new Observable.Transformer<T, T>() {
            //类似装饰者模式
            @Override
            public Observable<T> call(Observable<T> original) {
                return original.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        srl.post(new Runnable() {
                            @Override
                            public void run() {
                                srl.setRefreshing(true);
                            }
                        });
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        srl.post(new Runnable() {
                            @Override
                            public void run() {
                                srl.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        };
    }

    /**
     * 等待waiting
     *
     * @param pd
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> bindConnecting(final WaitingDialog pd) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> original) {
                return original.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        pd.show();
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        pd.hide();
                        pd.dismiss();
                    }
                }).doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        pd.hide();
                        pd.dismiss();
                    }
                });
            }
        };
    }

    /**
     * 是否显示等待waiting
     *
     * @param pd
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> bindConnecting(final WaitingDialog pd, final boolean showPd) {
        if (showPd) {
            return bindConnecting(pd);
        } else {
            return new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> original) {
                    return original.doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                        }
                    }).doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                        }
                    }).doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                        }
                    });
                }
            };
        }
    }
}
