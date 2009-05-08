package step.framework.extensions;

import java.util.Collections;
import java.util.List;


/**
 *  This class holds an unmodifiable extension list.
 */
class InterceptConfigData {

    private final List<Extension> extensionList;

    InterceptConfigData(List<Extension> extensionList) {
        this.extensionList = Collections.unmodifiableList(extensionList);
    }

    List<Extension> getExtensionList() {
        return this.extensionList;
    }

    /**
     * Returns a brief description of this class.
     * Format is subject to change.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.getClass().getSimpleName());
        sb.append(": ");
        sb.append("extensionList={");
        {
            int i = this.extensionList.size();
            for(Extension extension : this.extensionList) {
                sb.append(extension);
                if(--i != 0) {
                    sb.append(", ");
                }
            }
        }
        sb.append("}");
        sb.append(" ]");
        return sb.toString();
    }

}
