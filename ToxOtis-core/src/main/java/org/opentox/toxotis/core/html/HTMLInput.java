package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLInput extends HTMLComponent {

    public enum HTMLInputType {

        SUBMIT("submit"),
        TEXT("text"),
        HIDDEN("hidden"),
        BUTTON("button"),
        CHECKBOX("checkbox"),
        PASSWORD("password"),
        RADIO("radio"),
        RESET("reset");
        private String typeName;

        private HTMLInputType(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName;
        }
    }

    HTMLInput setType(HTMLInputType type);

    HTMLInput setName(String name);

    HTMLInput setValue(String value);

    HTMLInput setSize(int size);

    HTMLInput setMaxLength(int maxLength);
}
