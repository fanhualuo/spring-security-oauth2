
package com.hehe.common.util;

import com.google.common.base.Optional;
import com.hehe.common.model.Response;

import java.util.Collections;

/**
 * @author hehe
 */
public final class RespHelper {

    /**
     * Or.
     *
     * @param <T> the generic type
     * @param resp the resp
     * @param failValue the fail value
     * @return the t
     */
    public static <T> T or(Response<T> resp, T failValue) {
        return resp.isSuccess() ? resp.getResult() : failValue;
    }

    /**
     * Or false.
     *
     * @param resp the resp
     * @return the boolean
     */
    public static Boolean orFalse(Response<Boolean> resp) {
        return or(resp, Boolean.FALSE);
    }

    /**
     * Or 500.
     *
     * @param <T> the generic type
     * @param resp the resp
     * @return the t
     */
    public static <T> T or500(Response<T> resp) {
        if (resp.isSuccess()) {
            return resp.getResult();
        }
        throw new JsonResponseException(500, resp.getError());
    }

    /**
     * Or serv ex.
     *
     * @param <T> the generic type
     * @param resp the resp
     * @return the t
     */
    public static <T> T orServEx(Response<T> resp) {
        if (resp.isSuccess()) {
            return resp.getResult();
        }
        throw new ServiceException(resp.getError());
    }

    /**
     * need not to cast.
     *
     * @param <T> the generic type
     * @param <D> the generic type
     * @param data the data
     * @return the response
     */
    public static <T, D extends T> Response<T> ok(D data) {
        Response<T> resp = new Response<>();
        resp.setResult(data);
        return resp;
    }

    /**
     * Unwrap.
     *
     * @param <T> the generic type
     * @param resp the resp
     * @param error the error
     * @return the response
     * @deprecated use {@code RespHelper.Opt.unwrap(...)} instead
     */
    public static <T> Response<T> unwrap(Response<Optional<T>> resp, String error) {
        if (resp.isSuccess()) {
            if (resp.getResult().isPresent()) {
                return Response.ok(resp.getResult().get());
            }
            return Response.fail(error);
        }
        return Response.fail(resp.getError());
    }

    /**
     * Guava {@code Optional} Helpers.
     */
    public static final class Opt {
        
        /**
         * Unwrap.
         *
         * @param <T> the generic type
         * @param resp the resp
         * @param error the error
         * @return the response
         */
        public static <T> Response<T> unwrap(Response<Optional<T>> resp, String error) {
            if (resp.isSuccess()) {
                if (resp.getResult().isPresent()) {
                    return Response.ok(resp.getResult().get());
                }
                return Response.fail(error);
            }
            return Response.fail(resp.getError());
        }

        /**
         * Of.
         *
         * @param <T> the generic type
         * @param <D> the generic type
         * @param data the data
         * @return the response
         */
        public static <T, D extends T> Response<Optional<T>> of(D data) {
            return Response.ok(Optional.<T>of(data));
        }

        /**
         * Absent.
         *
         * @param <T> the generic type
         * @return the response
         */
        public static <T> Response<Optional<T>> absent() {
            return Response.ok(Optional.<T>absent());
        }

        /**
         * From nullable.
         *
         * @param <T> the generic type
         * @param <D> the generic type
         * @param data the data
         * @return the response
         */
        public static <T, D extends T> Response<Optional<T>> fromNullable(D data) {
            return Response.ok(Optional.<T>fromNullable(data));
        }
    }

    /**
     * Vanilla java {@code Map} Helpers.
     */
    public static final class Map {
        
        /**
         * Empty.
         *
         * @param <K> the key type
         * @param <V> the value type
         * @return the response
         */
        public static <K, V> Response<java.util.Map<K, V>> empty() {
            return Response.ok(Collections.<K, V>emptyMap());
        }
    }
}

