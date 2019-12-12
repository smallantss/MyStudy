package com.example.http;

public class Test {

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        final Request request = new Request.Builder()
                .url("https://www.wanandroid.com/lg/uncollect_originId/2333/json")
                .post(new RequestBody()
                        .addParams("id", "2334"))
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new CallBack() {
            @Override
            public void onSuccess(Call call, Response response) {
                System.out.println("result--->" + response.string());
            }

            @Override
            public void onFailure(Call call, Exception e) {
                System.out.println("onFailure--->");
            }
        });
    }
}
