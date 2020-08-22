/*
 * Copyright 2020 OmniFaces
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.util;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * This class provides access to (Java EE 6) platform services from the view point of JSF.
 * <p>
 * Note that this utility class can only be used in a JSF environment and is thus not
 * a Java EE general way to obtain platform services.
 *
 * @since 1.6
 * @author Arjan Tijms
 */
public final class Platform {

	// Constructors ---------------------------------------------------------------------------------------------------

	private Platform() {
		// Hide constructor.
	}


	// Bean Validation ------------------------------------------------------------------------------------------------

	/**
	 * Returns <code>true</code> if Bean Validation is available. This is remembered in the application scope.
	 * @return <code>true</code> if Bean Validation is available.
	 * @deprecated Since 3.8. Bean Validation utilities are migrated to {@link Validators}.
	 * Use {@link Validators#isBeanValidationAvailable()} instead.
	 */
	@Deprecated
	public static boolean isBeanValidationAvailable() {
		return Validators.isBeanValidationAvailable();
	}

	/**
	 * Returns the default bean validator factory. This is remembered in the application scope.
	 * @return The default bean validator factory.
	 * @deprecated Since 3.8. Bean Validation utilities are migrated to {@link Validators}.
	 * Use {@link Validators#getBeanValidatorFactory()} instead.
	 */
	@Deprecated
	public static ValidatorFactory getBeanValidatorFactory() {
		return Validators.getBeanValidatorFactory();
	}

	/**
	 * Returns the bean validator which is aware of the JSF locale.
	 * @return The bean validator which is aware of the JSF locale.
	 * @see Faces#getLocale()
	 * @deprecated Since 3.8. Bean Validation utilities are migrated to {@link Validators}.
	 * Use {@link Validators#getBeanValidator()} instead.
	 */
	@Deprecated
	public static Validator getBeanValidator() {
		return Validators.getBeanValidator();
	}

	/**
	 * Validate given bean on given group classes
	 * and return constraint violation messages mapped by property path.
	 * @param bean Bean to be validated.
	 * @param groups Bean validation groups, if any.
	 * @return Constraint violation messages mapped by property path.
	 * @since 2.7
	 * @deprecated Since 3.8. This method should have returned actual constraint violations instead of abstracting them.
	 * Use {@link Validators#validateBean(Object, Class...)} instead.
	 */
	@Deprecated
	public static Map<String, String> validateBean(Object bean, Class<?>... groups) {
		return mapViolationMessagesByPropertyPath(Validators.validateBean(bean, groups));
	}

	/**
	 * Validate given value as if it were a property of the given bean type
	 * and return constraint violation messages mapped by property path.
	 * @param beanType Type of target bean.
	 * @param propertyName Name of property on target bean.
	 * @param value Value to be validated.
	 * @param groups Bean validation groups, if any.
	 * @return Constraint violation messages mapped by property path.
	 * @since 2.7
	 * @deprecated Since 3.8. This method should have returned actual constraint violations instead of abstracting them.
	 * Use {@link Validators#validateBeanProperty(Class, String, Object, Class...)} instead.
	 */
	@Deprecated
	public static Map<String, String> validateBeanProperty(Class<?> beanType, String propertyName, Object value, Class<?>... groups) {
		return mapViolationMessagesByPropertyPath(Validators.validateBeanProperty(beanType, propertyName, value, groups));
	}

	private static Map<String, String> mapViolationMessagesByPropertyPath(Set<ConstraintViolation<?>> violations) {
		return violations.stream().collect(toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage, (l, r) -> l, LinkedHashMap::new));
	}


	// FacesServlet ---------------------------------------------------------------------------------------------------

	/**
	 * Returns the {@link ServletRegistration} associated with the {@link FacesServlet}.
	 * @param servletContext The context to get the ServletRegistration from.
	 * @return ServletRegistration for FacesServlet, or <code>null</code> if the FacesServlet is not installed.
	 * @since 1.8
	 */
	public static ServletRegistration getFacesServletRegistration(ServletContext servletContext) {
		ServletRegistration facesServletRegistration = null;

		for (ServletRegistration registration : servletContext.getServletRegistrations().values()) {
			if (registration.getClassName().equals(FacesServlet.class.getName())) {
				facesServletRegistration = registration;
				break;
			}
		}

		return facesServletRegistration;
	}

	/**
	 * Returns the mappings associated with the {@link FacesServlet}.
	 * @param servletContext The context to get the {@link FacesServlet} from.
	 * @return The mappings associated with the {@link FacesServlet}, or an empty set.
	 * @since 2.5
	 */
	public static Collection<String> getFacesServletMappings(ServletContext servletContext) {
		ServletRegistration facesServlet = getFacesServletRegistration(servletContext);
		return (facesServlet != null) ? facesServlet.getMappings() : Collections.<String>emptySet();
	}

}