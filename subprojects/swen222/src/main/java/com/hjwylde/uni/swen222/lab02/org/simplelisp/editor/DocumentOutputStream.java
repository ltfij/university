// This file is part of the Simple Lisp Interpreter.
//
// The Simple Lisp Interpreter is free software; you can
// redistribute it and/or modify it under the terms of the
// GNU General Public License as published by the Free Software
// Foundation; either version 2 of the License, or (at your
// option) any later version.
//
// The Simpular Program Interpreter is distributed in the hope
// that it will be useful, but WITHOUT ANY WARRANTY; without
// even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License
// for more details.
//
// You should have received a copy of the GNU General Public
// License along with the Simpular Program Interpreter; if not,
// write to the Free Software Foundation, Inc., 59 Temple Place,
// Suite 330, Boston, MA 02111-1307 USA
//
// (C) David James Pearce, 2005.

package com.hjwylde.uni.swen222.lab02.org.simplelisp.editor;

import java.io.OutputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class DocumentOutputStream extends OutputStream {
    
    private Document doc;
    private StringBuffer sb = new StringBuffer(" ");
    
    DocumentOutputStream(Document d) {
        if (d == null)
            throw new NullPointerException("Document cannot be null");
        
        doc = d;
    }
    
    @Override
    public void write(int b) {
        try {
            sb.setCharAt(0, (char) b);
            doc.insertString(doc.getLength(), sb.toString(), null);
        } catch (BadLocationException e) {
            // do nothing
        }
    }
}
