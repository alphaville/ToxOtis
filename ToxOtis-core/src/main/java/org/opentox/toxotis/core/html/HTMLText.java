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
package org.opentox.toxotis.core.html;

/**
 * Formatted text in an HTML document.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLText extends HTMLComponent {

    /**
     * Set the content of the text element. This is the text that will be displayed
     * on the HTML page.
     * @param text
     *      The text
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText setText(String text);

    /**
     * Set a comment instead of a text. This is done using the HTML comment
     * annotation <code>&lt;!-- comment --&gt;</code>
     * @param comment
     *      The comment
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText setComment(String comment);

    /**
     * Returns the text that was set to the current HTMLText object.
     * @return
     *      The text that was set to the current HTMLText object using the setter
     *      method {@link #setText(java.lang.String) } or alternatively
     *      {@link #setComment(java.lang.String) } or <code>null</code> if no
     *      text was provided.
     */
    String getText();

    /**
     * Format the text as bold using the HTML tag <code>&lt;b&gt;</code>
     * @param isBold
     *      Whehter the text should be formatted as bold.
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText formatBold(boolean isBold);

    /**
     * Format the text as italics using the HTML tag <code>&lt;em&gt;</code>
     * @param isItalics
     *      Whehter the text should be formatted as italics.
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText formatItalics(boolean isItalics);

    /**
     * Undeline the text using the HTML tag <code>&lt;u&gt;</code>
     * @param isUnderlined
     *      Whehter the text should be formatted as underlined.
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText formatUnderlined(boolean isUnderlined);

    /**
     * Format the text as true-type using the HTML tag <code>&lt;tt&gt;</code>
     * @param isTrueType 
     *      Whehter the text should be formatted as true-type.
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText formatTrueType(boolean isTrueType);

    /**
     * Encapsulates the formatted text in a <code>pre</code> block using the tag
     * <code>&lt;pre;gt;</code>
     * @param isPRE 
     *      Whehter the text should be formatted as bold.
     * @return
     *      The current modifiable updated HTMLText object
     */
    HTMLText formatPRE(boolean isPRE);
}
