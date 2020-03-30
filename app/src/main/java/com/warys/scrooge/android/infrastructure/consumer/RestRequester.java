package com.warys.scrooge.android.infrastructure.consumer;

import android.widget.TextView;
import com.warys.scrooge.android.infrastructure.common.Logger;
import com.warys.scrooge.android.infrastructure.consumer.model.ErrorResponse;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class RestRequester {

    private static final Logger LOGGER = new Logger(RestRequester.class);

    public static <T> void execute(Single<T> request, Consumer<T> callBack, final TextView errorTextView) {
        final Consumer<Throwable> throwableConsumer = getThrowableConsumer(errorTextView);
        request.subscribe(callBack, throwableConsumer).dispose();
    }

    public static <T> void execute(final Single<T> request, final TextView errorTextView) {
        final Consumer<Throwable> throwableConsumer = getThrowableConsumer(errorTextView);
        request.doOnError(throwableConsumer).subscribe();
    }

    private static Consumer<Throwable> getThrowableConsumer(final TextView errorTextView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws IOException {
                if (throwable instanceof HttpException) {
                    ResponseBody response = ((HttpException) throwable).response().errorBody();
                    final Converter<ResponseBody, ErrorResponse> converter = RestClient
                            .getInstance()
                            .getResponseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    final ErrorResponse errorResponse = converter.convert(response);
                    errorTextView.setText(errorResponse.getMessage());
                    LOGGER.error(errorResponse.toString());
                } else {
                    errorTextView.setText(throwable.getMessage());
                    LOGGER.error(throwable);
                }
            }
        };
    }
}
