/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.core.html.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLParagraph;
import org.opentox.toxotis.core.html.HTMLTag;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class HTMLTagImplTest {

    public HTMLTagImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testTags() {
        HTMLTag tag = new HTMLTagImpl("a");
        tag.addTagAttribute("href", Services.anonymous().toString());
        tag.setContent("My Link");
        assertEquals("<a href=\"http://anonymous.org/\">My Link</a>", tag.toString());
        assertEquals("a", tag.getTag());

        HTMLParagraph paragraph = new HTMLParagraphImpl();
        paragraph.addComponent(tag);
        paragraph.addComponent(tag);
        paragraph.setAlignment(Alignment.left);
        assertEquals("<p align=\"left\"><a href=\"http://anonymous.org/\">My Link</a>"
                + "<a href=\"http://anonymous.org/\">My Link</a></p>", paragraph.toString());
        assertEquals(Alignment.left, paragraph.getAlignment());
    }
}
