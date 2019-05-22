/**
 * slizaa-server-service-svg - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.server.service.svg.impl;


import com.google.common.base.Preconditions;

public class ImageKey {

    public static class DecodedKey {
        public boolean isOverlayImage = true;
        public String main;
        public String upperLeft;
        public String upperRight;
        public String lowerLeft;
        public String lowerRight;

        @Override
        public String toString() {
            return "DecodedKey{" +
                    "isOverlayImage=" + isOverlayImage +
                    ", main='" + main + '\'' +
                    ", upperLeft='" + upperLeft + '\'' +
                    ", upperRight='" + upperRight + '\'' +
                    ", lowerLeft='" + lowerLeft + '\'' +
                    ", lowerRight='" + lowerRight + '\'' +
                    '}';
        }
    }

    public static DecodedKey decode(String longKey) {
        Preconditions.checkNotNull(longKey);

        DecodedKey key = new DecodedKey();

        String[] values = longKey.split("\\?");
        key.main = values[0];

        if (values.length == 2) {
            values = values[1].split("&");
            for (String v : values) {
                String[] keyValue = v.split("=");
                switch (keyValue[0]) {
                    case "ol": {
                        key.isOverlayImage = false;
                    }
                    case "ul": {
                        key.upperLeft = keyValue[1];
                    }
                    case "ur": {
                        key.upperRight = keyValue[1];
                    }
                    case "ll": {
                        key.lowerLeft = keyValue[1];
                    }
                    case "lr": {
                        key.lowerRight = keyValue[1];
                    }
                }
            }
        }

        return key;
    }


    public static String longKey(boolean isOverlayImage, String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight) {

        StringBuilder result = new StringBuilder(Preconditions.checkNotNull(main, "Parameter 'main' must not be null."));

        if (notEmpty(upperLeft) || notEmpty(upperRight) || notEmpty(lowerLeft) || notEmpty(lowerRight) || !isOverlayImage) {
            result.append("?");
        }

        boolean prependAmpersand = false;
        if (!isOverlayImage) {
            prependAmpersand = append("ol", "0", prependAmpersand, result);
        }
        prependAmpersand = append("ul", upperLeft, prependAmpersand, result) || prependAmpersand;
        prependAmpersand = append("ur", upperRight, prependAmpersand, result) || prependAmpersand;
        prependAmpersand = append("ll", lowerLeft, prependAmpersand, result) || prependAmpersand;
        prependAmpersand = append("lr", lowerRight, prependAmpersand, result) || prependAmpersand;

        return result.toString();
    }

    public static String shortKey(String key) {
        byte[] bytes = Preconditions.checkNotNull(key, "Parameter 'key' must not be null.").getBytes();
        long k = 7;
        for (int i = 0; i < bytes.length; i++) {
            k *= 23;
            k += bytes[i];
            k *= 13;
            k %= 1000000009;
        }
        return k + "";
    }

    private static boolean append(String key, String value, boolean prependAmpersand, StringBuilder builder) {

        if (notEmpty(value)) {
            if (prependAmpersand) {
                builder.append("&");
            }
            builder.append(key + "=" + value);
            return true;
        }

        return false;
    }

    private static boolean notEmpty(String string) {
        return string != null && !string.isEmpty();
    }
}
