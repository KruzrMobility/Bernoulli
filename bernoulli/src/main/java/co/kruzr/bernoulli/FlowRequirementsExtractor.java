package co.kruzr.bernoulli;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.kruzr.bernoulli.annotation.RequiresPermission;
import co.kruzr.bernoulli.annotation.RequiresSetting;
import co.kruzr.bernoulli.annotation.repeatable.PermissionsRepeatable;
import co.kruzr.bernoulli.annotation.repeatable.SettingsRepeatable;
import lombok.Getter;

/**
 * This class is responsible for extracting the permission and setting requirements for a
 * given Stream object (i.e. method).
 */
class FlowRequirementsExtractor {

    /**
     * The method for which the requirements have to be extracted.
     */
    private Stream stream;

    /**
     * The list of annotations applied to the method.
     */
    private Annotation[] annotationList;

    /**
     * The list of permissions required by the method.
     */
    @Getter
    private List<RequiresPermission> listRequiresPermissions = new ArrayList<>();

    /**
     * The list of settings required by the method.
     */
    @Getter
    private List<RequiresSetting> listRequiresSettings = new ArrayList<>();

    /**
     * Determines whether the requirements of the method should be extracted, and if yes then initialises the stream
     * object.
     *
     * @param method the method for which the requirements have to be extracted
     */
    public FlowRequirementsExtractor(Method method) {

        annotationList = method.getAnnotations();
        stream = new Stream();
    }

    /**
     * Updates the stream object with the details of the permissions and settings it needs.
     *
     * @return the updated stream object if the requirements should be evaluated, null otherwise
     */
    public Stream getRequirementsOfStream() {

        Log.e("Bernoulli", annotationList.toString());

        for (Annotation annotation : annotationList) {

            if (annotation instanceof PermissionsRepeatable)
                listRequiresPermissions.addAll(Arrays.asList(((PermissionsRepeatable) annotation).value()));

            if (annotation instanceof SettingsRepeatable)
                listRequiresSettings.addAll(Arrays.asList(((SettingsRepeatable) annotation).value()));

            if (annotation instanceof RequiresPermission)
                listRequiresPermissions.add((RequiresPermission) annotation);

            if (annotation instanceof RequiresSetting)
                listRequiresSettings.add((RequiresSetting) annotation);
        }

        stream.setRequiredPermissions(listRequiresPermissions);
        stream.setRequiredSettings(listRequiresSettings);

        PrintUtils.printRequiredPermissionsAndSettings(stream);
        return stream;
    }
}