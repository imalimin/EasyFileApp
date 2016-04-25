##更新头像
```
public void updateHead(String path, final OnUpdateHeadListener onUpdateHeadListener) {
        File file = new File(path);
        if (!file.exists() || file.length() > MAX_SIZE) {
            onUpdateHeadListener.onError("图片大小不能超过1M");
            return;
        }
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.UPDATE_HEAD_PHOTO
        , new StringHttpTask.HttpExecuteLinstener<String>() {

            @Override
            public void onSuccess(String result) {
                Log.v(UserModelImpl.class, "result=" + result);
                onUpdateHeadListener.onSuccess(result);
            }

            @Override
            public void onError(int code, String msg) {
                onUpdateHeadListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addFile("file", file);
        HttpService.instance().execute(task);
}
```

##更新用户信息
```
public void updateInfo(String introduce, String name, String type, final OnUpdateInfoListener onUpdateInfoListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.UPDATE_USER
        , new StringHttpTask.HttpExecuteLinstener<String>() {

            @Override
            public void onSuccess(String result) {
                Log.v(UserModelImpl.class, "result=" + result);
                onUpdateInfoListener.onSuccess();
            }

            @Override
            public void onError(int code, String msg) {
                onUpdateInfoListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("introduce", introduce).addParam("name", name);
        HttpService.instance().execute(task);
}
```

```
public void delUser(String studentId, final OnCommonListener onCommonListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.DEL_USER
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(UserModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onCommonListener.onError(MyApplication.instance().getApplicationContext().getResources()
                    .getString(R.string.Service_Error));
                } else {
                    onCommonListener.onSuccess("已删除");
                }
            }

            @Override
            public void onError(int code, String msg) {
                onCommonListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("studentId", studentId);
        HttpService.instance().execute(task);
}
```

```
public void queryAllUser(final OnHRQueryAllUserListener onHRQueryAllUserListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_GET, Api.ALL_USER
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(HRModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onHRQueryAllUserListener.onError(MyApplication.instance().getApplicationContext().getResources()
                    .getString(R.string.Service_Error));
                } else {
                    UserArray ua = new GsonBuilder().create().fromJson(result, UserArray.class);
                    onHRQueryAllUserListener.onSuccess(ua.getAllUser());
                }
            }

            @Override
            public void onError(int code, String msg) {
                onHRQueryAllUserListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        HttpService.instance().execute(task);
}
```

```
public void addUser(User user, final OnAddUserListener onAddUserListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.ADD_USER
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.contains(Api.ERROR_JSON)) {
                    onAddUserListener.onError(MyApplication.instance().getApplicationContext().getResources()
                    .getString(R.string.Service_Error));
                } else {
                    UserArray ua = new GsonBuilder().create().fromJson(result, UserArray.class);
                    onAddUserListener.onSuccess();
                }
            }

            @Override
            public void onError(int code, String msg) {
                onAddUserListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("name", user.getName()).addParam("studentId", user.getStudentId()).addParam("passWord", user.getPassword()).addParam("userType", user.getUserType());
        HttpService.instance().execute(task);
}
```

```
public void login(String userName, String password, OnLoginListener onLoginListener) {
        this.mLoginListener = onLoginListener;
        task.addParam("studentId", userName).addParam("passWord", password);
        HttpService.instance().execute(task);
    }

private HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.LOGIN
, new StringHttpTask.HttpExecuteLinstener<String>() {
        @Override
        public void onSuccess(String result) {
            Log.v(LoginModelImpl.class, "result=" + result);
            if (result.contains(Api.ERROR_FAIL)) {
                mLoginListener.onError(result);
            } else {
                User user = new GsonBuilder().create().fromJson(result, User.class);
                MyApplication.instance().getDBService().getCookieModel()
                .save(HttpCookie.from(HttpService.instance().getCookies()));
                MyApplication.instance().getDBService().getUserModel().save(user);
                mLoginListener.onSuccess(user);
            }
        }

        @Override
        public void onError(int code, String msg) {
            mLoginListener.onError(msg + ":" + code);
//            showProgress(false);
//            Snackbar.make(mUserView, msg + ":" + code, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onProgress(int progress) {

        }
});
```

```
public void queryAllFood(final OnQueryFoodListener onQueryFoodListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_GET, Api.ALL_FOOD
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.contains(Api.ERROR_JSON)) {
                    onQueryFoodListener.onError(MyApplication.instance().getApplicationContext()
                    .getResources().getString(R.string.Service_Error));
                } else {
                    FoodArray fa = new GsonBuilder().create().fromJson(result, FoodArray.class);
                    if (fa != null)
                        onQueryFoodListener.onSuccess(fa.getFoods());
                    else
                        onQueryFoodListener.onError(MyApplication.instance().getApplicationContext()
                        .getResources().getString(R.string.Service_Error));
                }
            }

            @Override
            public void onError(int code, String msg) {
                onQueryFoodListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        HttpService.instance().execute(task);
}
```

```
public void addFood(Food food, final OnCommonListener onCommonListener) {
        File file = new File(food.getImages()[0]);
        if (!file.exists()) return;
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.ADD_FOOD
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(FoodModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onCommonListener.onError(MyApplication.instance().getApplicationContext()
                    .getResources().getString(R.string.Service_Error));
                } else {
                    onCommonListener.onSuccess("");
                }
            }

            @Override
            public void onError(int code, String msg) {
                onCommonListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("foodName", food.getFoodName()).addParam("introduction", food.getIntroduction())
        .addParam("price", food.getPrice()).addFile("file", file);
        HttpService.instance().execute(task);
}
```

```
public void queryAllBill(final OnQueryBillListener onQueryBillListener) {
        User user = MyApplication.instance().getDBService().getUserModel().get();
        String url = Api.ALL_BILL;
        if (!user.getUserType().equals(UserType.Administrator))
            url = Api.ALL_BILL_STATE + "?state=" + BillState.CREATED;
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_GET, url
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(BillModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onQueryBillListener.onError(MyApplication.instance().getApplicationContext()
                    .getResources().getString(R.string.Service_Error));
                } else {
                    BillArray ba = new GsonBuilder().create().fromJson(result, BillArray.class);
                    if (ba != null)
                        onQueryBillListener.onSuccess(ba.getMyBills());
                    else
                        onQueryBillListener.onError(MyApplication.instance().getApplicationContext()
                        .getResources().getString(R.string.Service_Error));
                }
            }

            @Override
            public void onError(int code, String msg) {
                onQueryBillListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        HttpService.instance().execute(task);
}
```

```
public void addBill(Bill bill, final OnCommonListener onCommonListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.ADD_BILL
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(BillModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onCommonListener.onError(MyApplication.instance().getApplicationContext().getResources()
                    .getString(R.string.Service_Error));
                } else {
                    onCommonListener.onSuccess("");
                }
            }

            @Override
            public void onError(int code, String msg) {
                onCommonListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        for (Food f : bill.getFoods()) {
            task.addParam("foodsId", String.valueOf(f.getId()));
        }
        HttpService.instance().execute(task);
}
```

```
public void updateBill(Bill bill, final OnCommonListener onCommonListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.UPDATE_BILL
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(BillModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onCommonListener.onError(MyApplication.instance().getApplicationContext().getResources()
                    .getString(R.string.Service_Error));
                } else {
                    onCommonListener.onSuccess("");
                }
            }

            @Override
            public void onError(int code, String msg) {
                onCommonListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("state", String.valueOf(bill.getState()))
        .addParam("billId", String.valueOf(bill.getId()));
        HttpService.instance().execute(task);
}
```

```
public void billCount(final OnBillCountListener onBillCountListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_GET, Api.ALL_BILL
        , new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(BillModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onBillCountListener.onError(MyApplication.instance().getApplicationContext()
                    .getResources().getString(R.string.Service_Error));
                } else {
                    BillArray ba = new GsonBuilder().create().fromJson(result, BillArray.class);
                    if (ba != null) {
                        double[] r = count(ba.getMyBills());
                        onBillCountListener.onSuccess((int)r[0], r[1]);
                    } else
                        onBillCountListener.onError(MyApplication.instance().getApplicationContext()
                        .getResources().getString(R.string.Service_Error));
                }
            }

            @Override
            public void onError(int code, String msg) {
                onBillCountListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        HttpService.instance().execute(task);
}
```

```
User user = getApplicationFunc().getDBService().getUserModel().get();
```
