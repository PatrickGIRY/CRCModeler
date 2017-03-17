package crc.modeler.domain;

import java.util.Objects;

public class CRCCard {
    private final ClassName className;

    private CRCCard(ClassName className) {
        this.className = className;
    }

    public static CRCCardBuilder aCRCCard() {
        return new CRCCardBuilder();
    }

    public boolean hasClassName(ClassName className) {
        return Objects.equals(this.className, className);
    }

    public static final class CRCCardBuilder {

        private ClassName className;

        private CRCCardBuilder() {
        }

        public CRCCardBuilder withClassName(ClassName className) {
            this.className = className;
            return this;
        }

        public CRCCard build() {
            return new CRCCard(className);
        }
    }
}
