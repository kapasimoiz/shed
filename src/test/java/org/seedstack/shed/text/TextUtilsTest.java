/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.shed.text;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextUtilsTest {
    private String longString = "Nec vox accusatoris ulla licet subditicii in his malorum quaerebatur acervis ut saltem specie tenus crimina praescriptis legum committerentur, quod aliquotiens fecere principes saevi: sed quicquid Caesaris implacabilitati sedisset, id velut fas iusque perpensum confestim urgebatur impleri.";

    @Test
    public void splitWords() throws Exception {
        String words[] = TextUtils.splitWords(longString);
        assertThat(String.join(" ", (CharSequence[]) words)).isEqualTo(longString);
    }

    @Test
    public void simpleWrap() throws Exception {
        String wrapped = TextUtils.wrap(longString, 80);
        for (String line : wrapped.split("\n")) {
            assertThat(line.length()).isLessThanOrEqualTo(80);
        }
    }

    @Test
    public void wrapPreserveShortStrings() throws Exception {
        String stringWithNewLine = "Nec vox accusatoris\n";
        assertThat(TextUtils.wrap(stringWithNewLine, 80)).isEqualTo(stringWithNewLine);
        String stringWithoutNewLine = "Nec vox accusatoris";
        assertThat(TextUtils.wrap(stringWithoutNewLine, 80)).isEqualTo(stringWithoutNewLine);
    }

    @Test
    public void longWordWrap() throws Exception {
        String longWord = "abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz----abcdefghijklmnopqrstuvwxyz----";
        String wrapped = TextUtils.wrap(longWord, 80);
        for (String line : wrapped.split("\n")) {
            assertThat(line.length()).isLessThanOrEqualTo(80);
        }
        assertThat(wrapped).contains("—");
    }

    @Test
    public void leftPad() throws Exception {
        String multiLineString = "abcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz";
        String padded = TextUtils.leftPad(multiLineString, "***", 1);
        boolean first = true;
        for (String s : padded.split("\\n")) {
            if (first) {
                assertThat(s).isEqualTo("abcdefghijklmnopqrstuvwxyz");
                first = false;
            } else {
                assertThat(s).isEqualTo("***abcdefghijklmnopqrstuvwxyz");
            }
        }
    }
}