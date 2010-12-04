package org.opentox.toxotis.core.html;

/**
 * Formatted text in an HTML document.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLText extends HTMLComponent {

    HTMLText setText(String text);

    HTMLText setComment(String text);

    String getText();

    HTMLText formatBold(boolean isBold);

    HTMLText formatItalics(boolean isItalics);

    HTMLText formatUnderlined(boolean isUnderlined);

    HTMLText formatTrueType(boolean isTrueType);

    HTMLText formatPRE(boolean isPRE);
}
