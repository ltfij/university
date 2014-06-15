// This file is part of the WhileLang Compiler (wlc).
//
// The WhileLang Compiler is free software; you can redistribute
// it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
//
// The WhileLang Compiler is distributed in the hope that it
// will be useful, but WITHOUT ANY WARRANTY; without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE. See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public
// License along with the WhileLang Compiler. If not, see
// <http://www.gnu.org/licenses/>
//
// Copyright 2013, David James Pearce.

package whilelang.util;

import whilelang.lang.Type;

/**
 * This exception is thrown when an error occurs during the interpretation of a {@code While} file.
 *
 * @author Henry J. Wylde
 */
public class RuntimeError extends RuntimeException {

    public RuntimeError() {
        super();
    }

    public RuntimeError(String message) {
        super(message);
    }

    public RuntimeError(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeError(Throwable cause) {
        super(cause);
    }

    public static RuntimeError castError(Object obj, Type type) {
        return new RuntimeError("invalid cast from obj (" + obj + ") to " + type);
    }
}

