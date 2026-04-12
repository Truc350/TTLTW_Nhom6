package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final JakartaLibraryAccessors laccForJakartaLibraryAccessors = new JakartaLibraryAccessors(owner);
    private final MysqlLibraryAccessors laccForMysqlLibraryAccessors = new MysqlLibraryAccessors(owner);
    private final OrgLibraryAccessors laccForOrgLibraryAccessors = new OrgLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>com</b>
     */
    public ComLibraryAccessors getCom() {
        return laccForComLibraryAccessors;
    }

    /**
     * Group of libraries at <b>jakarta</b>
     */
    public JakartaLibraryAccessors getJakarta() {
        return laccForJakartaLibraryAccessors;
    }

    /**
     * Group of libraries at <b>mysql</b>
     */
    public MysqlLibraryAccessors getMysql() {
        return laccForMysqlLibraryAccessors;
    }

    /**
     * Group of libraries at <b>org</b>
     */
    public OrgLibraryAccessors getOrg() {
        return laccForOrgLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComCloudinaryLibraryAccessors laccForComCloudinaryLibraryAccessors = new ComCloudinaryLibraryAccessors(owner);
        private final ComFasterxmlLibraryAccessors laccForComFasterxmlLibraryAccessors = new ComFasterxmlLibraryAccessors(owner);
        private final ComGoogleLibraryAccessors laccForComGoogleLibraryAccessors = new ComGoogleLibraryAccessors(owner);
        private final ComSunLibraryAccessors laccForComSunLibraryAccessors = new ComSunLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.cloudinary</b>
         */
        public ComCloudinaryLibraryAccessors getCloudinary() {
            return laccForComCloudinaryLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.fasterxml</b>
         */
        public ComFasterxmlLibraryAccessors getFasterxml() {
            return laccForComFasterxmlLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.google</b>
         */
        public ComGoogleLibraryAccessors getGoogle() {
            return laccForComGoogleLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.sun</b>
         */
        public ComSunLibraryAccessors getSun() {
            return laccForComSunLibraryAccessors;
        }

    }

    public static class ComCloudinaryLibraryAccessors extends SubDependencyFactory {
        private final ComCloudinaryCloudinaryLibraryAccessors laccForComCloudinaryCloudinaryLibraryAccessors = new ComCloudinaryCloudinaryLibraryAccessors(owner);

        public ComCloudinaryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.cloudinary.cloudinary</b>
         */
        public ComCloudinaryCloudinaryLibraryAccessors getCloudinary() {
            return laccForComCloudinaryCloudinaryLibraryAccessors;
        }

    }

    public static class ComCloudinaryCloudinaryLibraryAccessors extends SubDependencyFactory {

        public ComCloudinaryCloudinaryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>http44</b> with <b>com.cloudinary:cloudinary-http44</b> coordinates and
         * with version reference <b>com.cloudinary.cloudinary.http44</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHttp44() {
            return create("com.cloudinary.cloudinary.http44");
        }

    }

    public static class ComFasterxmlLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonLibraryAccessors laccForComFasterxmlJacksonLibraryAccessors = new ComFasterxmlJacksonLibraryAccessors(owner);

        public ComFasterxmlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.fasterxml.jackson</b>
         */
        public ComFasterxmlJacksonLibraryAccessors getJackson() {
            return laccForComFasterxmlJacksonLibraryAccessors;
        }

    }

    public static class ComFasterxmlJacksonLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonCoreLibraryAccessors laccForComFasterxmlJacksonCoreLibraryAccessors = new ComFasterxmlJacksonCoreLibraryAccessors(owner);

        public ComFasterxmlJacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.fasterxml.jackson.core</b>
         */
        public ComFasterxmlJacksonCoreLibraryAccessors getCore() {
            return laccForComFasterxmlJacksonCoreLibraryAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonCoreJacksonLibraryAccessors laccForComFasterxmlJacksonCoreJacksonLibraryAccessors = new ComFasterxmlJacksonCoreJacksonLibraryAccessors(owner);

        public ComFasterxmlJacksonCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.fasterxml.jackson.core.jackson</b>
         */
        public ComFasterxmlJacksonCoreJacksonLibraryAccessors getJackson() {
            return laccForComFasterxmlJacksonCoreJacksonLibraryAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreJacksonLibraryAccessors extends SubDependencyFactory {

        public ComFasterxmlJacksonCoreJacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>databind</b> with <b>com.fasterxml.jackson.core:jackson-databind</b> coordinates and
         * with version reference <b>com.fasterxml.jackson.core.jackson.databind</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDatabind() {
            return create("com.fasterxml.jackson.core.jackson.databind");
        }

    }

    public static class ComGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleCodeLibraryAccessors laccForComGoogleCodeLibraryAccessors = new ComGoogleCodeLibraryAccessors(owner);

        public ComGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.google.code</b>
         */
        public ComGoogleCodeLibraryAccessors getCode() {
            return laccForComGoogleCodeLibraryAccessors;
        }

    }

    public static class ComGoogleCodeLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleCodeGsonLibraryAccessors laccForComGoogleCodeGsonLibraryAccessors = new ComGoogleCodeGsonLibraryAccessors(owner);

        public ComGoogleCodeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.google.code.gson</b>
         */
        public ComGoogleCodeGsonLibraryAccessors getGson() {
            return laccForComGoogleCodeGsonLibraryAccessors;
        }

    }

    public static class ComGoogleCodeGsonLibraryAccessors extends SubDependencyFactory {

        public ComGoogleCodeGsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>gson</b> with <b>com.google.code.gson:gson</b> coordinates and
         * with version reference <b>com.google.code.gson.gson</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGson() {
            return create("com.google.code.gson.gson");
        }

    }

    public static class ComSunLibraryAccessors extends SubDependencyFactory {
        private final ComSunMailLibraryAccessors laccForComSunMailLibraryAccessors = new ComSunMailLibraryAccessors(owner);

        public ComSunLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.sun.mail</b>
         */
        public ComSunMailLibraryAccessors getMail() {
            return laccForComSunMailLibraryAccessors;
        }

    }

    public static class ComSunMailLibraryAccessors extends SubDependencyFactory {
        private final ComSunMailJakartaLibraryAccessors laccForComSunMailJakartaLibraryAccessors = new ComSunMailJakartaLibraryAccessors(owner);

        public ComSunMailLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.sun.mail.jakarta</b>
         */
        public ComSunMailJakartaLibraryAccessors getJakarta() {
            return laccForComSunMailJakartaLibraryAccessors;
        }

    }

    public static class ComSunMailJakartaLibraryAccessors extends SubDependencyFactory {

        public ComSunMailJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mail</b> with <b>com.sun.mail:jakarta.mail</b> coordinates and
         * with version reference <b>com.sun.mail.jakarta.mail</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMail() {
            return create("com.sun.mail.jakarta.mail");
        }

    }

    public static class JakartaLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletLibraryAccessors laccForJakartaServletLibraryAccessors = new JakartaServletLibraryAccessors(owner);

        public JakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet</b>
         */
        public JakartaServletLibraryAccessors getServlet() {
            return laccForJakartaServletLibraryAccessors;
        }

    }

    public static class JakartaServletLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJakartaLibraryAccessors laccForJakartaServletJakartaLibraryAccessors = new JakartaServletJakartaLibraryAccessors(owner);
        private final JakartaServletJspLibraryAccessors laccForJakartaServletJspLibraryAccessors = new JakartaServletJspLibraryAccessors(owner);

        public JakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jakarta</b>
         */
        public JakartaServletJakartaLibraryAccessors getJakarta() {
            return laccForJakartaServletJakartaLibraryAccessors;
        }

        /**
         * Group of libraries at <b>jakarta.servlet.jsp</b>
         */
        public JakartaServletJspLibraryAccessors getJsp() {
            return laccForJakartaServletJspLibraryAccessors;
        }

    }

    public static class JakartaServletJakartaLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJakartaServletLibraryAccessors laccForJakartaServletJakartaServletLibraryAccessors = new JakartaServletJakartaServletLibraryAccessors(owner);

        public JakartaServletJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jakarta.servlet</b>
         */
        public JakartaServletJakartaServletLibraryAccessors getServlet() {
            return laccForJakartaServletJakartaServletLibraryAccessors;
        }

    }

    public static class JakartaServletJakartaServletLibraryAccessors extends SubDependencyFactory {

        public JakartaServletJakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>jakarta.servlet:jakarta.servlet-api</b> coordinates and
         * with version reference <b>jakarta.servlet.jakarta.servlet.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("jakarta.servlet.jakarta.servlet.api");
        }

    }

    public static class JakartaServletJspLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlLibraryAccessors laccForJakartaServletJspJstlLibraryAccessors = new JakartaServletJspJstlLibraryAccessors(owner);

        public JakartaServletJspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jsp.jstl</b>
         */
        public JakartaServletJspJstlLibraryAccessors getJstl() {
            return laccForJakartaServletJspJstlLibraryAccessors;
        }

    }

    public static class JakartaServletJspJstlLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaLibraryAccessors laccForJakartaServletJspJstlJakartaLibraryAccessors = new JakartaServletJspJstlJakartaLibraryAccessors(owner);

        public JakartaServletJspJstlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jsp.jstl.jakarta</b>
         */
        public JakartaServletJspJstlJakartaLibraryAccessors getJakarta() {
            return laccForJakartaServletJspJstlJakartaLibraryAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaServletLibraryAccessors laccForJakartaServletJspJstlJakartaServletLibraryAccessors = new JakartaServletJspJstlJakartaServletLibraryAccessors(owner);

        public JakartaServletJspJstlJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jsp.jstl.jakarta.servlet</b>
         */
        public JakartaServletJspJstlJakartaServletLibraryAccessors getServlet() {
            return laccForJakartaServletJspJstlJakartaServletLibraryAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaServletLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaServletJspLibraryAccessors laccForJakartaServletJspJstlJakartaServletJspLibraryAccessors = new JakartaServletJspJstlJakartaServletJspLibraryAccessors(owner);

        public JakartaServletJspJstlJakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jsp.jstl.jakarta.servlet.jsp</b>
         */
        public JakartaServletJspJstlJakartaServletJspLibraryAccessors getJsp() {
            return laccForJakartaServletJspJstlJakartaServletJspLibraryAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaServletJspLibraryAccessors extends SubDependencyFactory {
        private final JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors laccForJakartaServletJspJstlJakartaServletJspJstlLibraryAccessors = new JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors(owner);

        public JakartaServletJspJstlJakartaServletJspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl</b>
         */
        public JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors getJstl() {
            return laccForJakartaServletJspJstlJakartaServletJspJstlLibraryAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors extends SubDependencyFactory {

        public JakartaServletJspJstlJakartaServletJspJstlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api</b> coordinates and
         * with version reference <b>jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api");
        }

    }

    public static class MysqlLibraryAccessors extends SubDependencyFactory {
        private final MysqlMysqlLibraryAccessors laccForMysqlMysqlLibraryAccessors = new MysqlMysqlLibraryAccessors(owner);

        public MysqlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>mysql.mysql</b>
         */
        public MysqlMysqlLibraryAccessors getMysql() {
            return laccForMysqlMysqlLibraryAccessors;
        }

    }

    public static class MysqlMysqlLibraryAccessors extends SubDependencyFactory {
        private final MysqlMysqlConnectorLibraryAccessors laccForMysqlMysqlConnectorLibraryAccessors = new MysqlMysqlConnectorLibraryAccessors(owner);

        public MysqlMysqlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>mysql.mysql.connector</b>
         */
        public MysqlMysqlConnectorLibraryAccessors getConnector() {
            return laccForMysqlMysqlConnectorLibraryAccessors;
        }

    }

    public static class MysqlMysqlConnectorLibraryAccessors extends SubDependencyFactory {

        public MysqlMysqlConnectorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>java</b> with <b>mysql:mysql-connector-java</b> coordinates and
         * with version reference <b>mysql.mysql.connector.java</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJava() {
            return create("mysql.mysql.connector.java");
        }

    }

    public static class OrgLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishLibraryAccessors laccForOrgGlassfishLibraryAccessors = new OrgGlassfishLibraryAccessors(owner);
        private final OrgJdbiLibraryAccessors laccForOrgJdbiLibraryAccessors = new OrgJdbiLibraryAccessors(owner);
        private final OrgJsonLibraryAccessors laccForOrgJsonLibraryAccessors = new OrgJsonLibraryAccessors(owner);
        private final OrgJunitLibraryAccessors laccForOrgJunitLibraryAccessors = new OrgJunitLibraryAccessors(owner);
        private final OrgMindrotLibraryAccessors laccForOrgMindrotLibraryAccessors = new OrgMindrotLibraryAccessors(owner);
        private final OrgSlf4jLibraryAccessors laccForOrgSlf4jLibraryAccessors = new OrgSlf4jLibraryAccessors(owner);

        public OrgLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish</b>
         */
        public OrgGlassfishLibraryAccessors getGlassfish() {
            return laccForOrgGlassfishLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jdbi</b>
         */
        public OrgJdbiLibraryAccessors getJdbi() {
            return laccForOrgJdbiLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.json</b>
         */
        public OrgJsonLibraryAccessors getJson() {
            return laccForOrgJsonLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.junit</b>
         */
        public OrgJunitLibraryAccessors getJunit() {
            return laccForOrgJunitLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.mindrot</b>
         */
        public OrgMindrotLibraryAccessors getMindrot() {
            return laccForOrgMindrotLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.slf4j</b>
         */
        public OrgSlf4jLibraryAccessors getSlf4j() {
            return laccForOrgSlf4jLibraryAccessors;
        }

    }

    public static class OrgGlassfishLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebLibraryAccessors laccForOrgGlassfishWebLibraryAccessors = new OrgGlassfishWebLibraryAccessors(owner);

        public OrgGlassfishLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish.web</b>
         */
        public OrgGlassfishWebLibraryAccessors getWeb() {
            return laccForOrgGlassfishWebLibraryAccessors;
        }

    }

    public static class OrgGlassfishWebLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebJakartaLibraryAccessors laccForOrgGlassfishWebJakartaLibraryAccessors = new OrgGlassfishWebJakartaLibraryAccessors(owner);

        public OrgGlassfishWebLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish.web.jakarta</b>
         */
        public OrgGlassfishWebJakartaLibraryAccessors getJakarta() {
            return laccForOrgGlassfishWebJakartaLibraryAccessors;
        }

    }

    public static class OrgGlassfishWebJakartaLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebJakartaServletLibraryAccessors laccForOrgGlassfishWebJakartaServletLibraryAccessors = new OrgGlassfishWebJakartaServletLibraryAccessors(owner);

        public OrgGlassfishWebJakartaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish.web.jakarta.servlet</b>
         */
        public OrgGlassfishWebJakartaServletLibraryAccessors getServlet() {
            return laccForOrgGlassfishWebJakartaServletLibraryAccessors;
        }

    }

    public static class OrgGlassfishWebJakartaServletLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishWebJakartaServletJspLibraryAccessors laccForOrgGlassfishWebJakartaServletJspLibraryAccessors = new OrgGlassfishWebJakartaServletJspLibraryAccessors(owner);

        public OrgGlassfishWebJakartaServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish.web.jakarta.servlet.jsp</b>
         */
        public OrgGlassfishWebJakartaServletJspLibraryAccessors getJsp() {
            return laccForOrgGlassfishWebJakartaServletJspLibraryAccessors;
        }

    }

    public static class OrgGlassfishWebJakartaServletJspLibraryAccessors extends SubDependencyFactory {

        public OrgGlassfishWebJakartaServletJspLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jstl</b> with <b>org.glassfish.web:jakarta.servlet.jsp.jstl</b> coordinates and
         * with version reference <b>org.glassfish.web.jakarta.servlet.jsp.jstl</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJstl() {
            return create("org.glassfish.web.jakarta.servlet.jsp.jstl");
        }

    }

    public static class OrgJdbiLibraryAccessors extends SubDependencyFactory {
        private final OrgJdbiJdbi3LibraryAccessors laccForOrgJdbiJdbi3LibraryAccessors = new OrgJdbiJdbi3LibraryAccessors(owner);

        public OrgJdbiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jdbi.jdbi3</b>
         */
        public OrgJdbiJdbi3LibraryAccessors getJdbi3() {
            return laccForOrgJdbiJdbi3LibraryAccessors;
        }

    }

    public static class OrgJdbiJdbi3LibraryAccessors extends SubDependencyFactory {

        public OrgJdbiJdbi3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.jdbi:jdbi3-core</b> coordinates and
         * with version reference <b>org.jdbi.jdbi3.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.jdbi.jdbi3.core");
        }

        /**
         * Dependency provider for <b>sqlobject</b> with <b>org.jdbi:jdbi3-sqlobject</b> coordinates and
         * with version reference <b>org.jdbi.jdbi3.sqlobject</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSqlobject() {
            return create("org.jdbi.jdbi3.sqlobject");
        }

    }

    public static class OrgJsonLibraryAccessors extends SubDependencyFactory {

        public OrgJsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>json</b> with <b>org.json:json</b> coordinates and
         * with version reference <b>org.json.json</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJson() {
            return create("org.json.json");
        }

    }

    public static class OrgJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterLibraryAccessors laccForOrgJunitJupiterLibraryAccessors = new OrgJunitJupiterLibraryAccessors(owner);

        public OrgJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.junit.jupiter</b>
         */
        public OrgJunitJupiterLibraryAccessors getJupiter() {
            return laccForOrgJunitJupiterLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitLibraryAccessors laccForOrgJunitJupiterJunitLibraryAccessors = new OrgJunitJupiterJunitLibraryAccessors(owner);

        public OrgJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.junit.jupiter.junit</b>
         */
        public OrgJunitJupiterJunitLibraryAccessors getJunit() {
            return laccForOrgJunitJupiterJunitLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitJupiterLibraryAccessors laccForOrgJunitJupiterJunitJupiterLibraryAccessors = new OrgJunitJupiterJunitJupiterLibraryAccessors(owner);

        public OrgJunitJupiterJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.junit.jupiter.junit.jupiter</b>
         */
        public OrgJunitJupiterJunitJupiterLibraryAccessors getJupiter() {
            return laccForOrgJunitJupiterJunitJupiterLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterJunitJupiterLibraryAccessors extends SubDependencyFactory {

        public OrgJunitJupiterJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>org.junit.jupiter:junit-jupiter-api</b> coordinates and
         * with version reference <b>org.junit.jupiter.junit.jupiter.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("org.junit.jupiter.junit.jupiter.api");
        }

        /**
         * Dependency provider for <b>engine</b> with <b>org.junit.jupiter:junit-jupiter-engine</b> coordinates and
         * with version reference <b>org.junit.jupiter.junit.jupiter.engine</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getEngine() {
            return create("org.junit.jupiter.junit.jupiter.engine");
        }

    }

    public static class OrgMindrotLibraryAccessors extends SubDependencyFactory {

        public OrgMindrotLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jbcrypt</b> with <b>org.mindrot:jbcrypt</b> coordinates and
         * with version reference <b>org.mindrot.jbcrypt</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJbcrypt() {
            return create("org.mindrot.jbcrypt");
        }

    }

    public static class OrgSlf4jLibraryAccessors extends SubDependencyFactory {
        private final OrgSlf4jSlf4jLibraryAccessors laccForOrgSlf4jSlf4jLibraryAccessors = new OrgSlf4jSlf4jLibraryAccessors(owner);

        public OrgSlf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.slf4j.slf4j</b>
         */
        public OrgSlf4jSlf4jLibraryAccessors getSlf4j() {
            return laccForOrgSlf4jSlf4jLibraryAccessors;
        }

    }

    public static class OrgSlf4jSlf4jLibraryAccessors extends SubDependencyFactory {

        public OrgSlf4jSlf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>simple</b> with <b>org.slf4j:slf4j-simple</b> coordinates and
         * with version reference <b>org.slf4j.slf4j.simple</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSimple() {
            return create("org.slf4j.slf4j.simple");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ComVersionAccessors vaccForComVersionAccessors = new ComVersionAccessors(providers, config);
        private final JakartaVersionAccessors vaccForJakartaVersionAccessors = new JakartaVersionAccessors(providers, config);
        private final MysqlVersionAccessors vaccForMysqlVersionAccessors = new MysqlVersionAccessors(providers, config);
        private final OrgVersionAccessors vaccForOrgVersionAccessors = new OrgVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com</b>
         */
        public ComVersionAccessors getCom() {
            return vaccForComVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.jakarta</b>
         */
        public JakartaVersionAccessors getJakarta() {
            return vaccForJakartaVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.mysql</b>
         */
        public MysqlVersionAccessors getMysql() {
            return vaccForMysqlVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org</b>
         */
        public OrgVersionAccessors getOrg() {
            return vaccForOrgVersionAccessors;
        }

    }

    public static class ComVersionAccessors extends VersionFactory  {

        private final ComCloudinaryVersionAccessors vaccForComCloudinaryVersionAccessors = new ComCloudinaryVersionAccessors(providers, config);
        private final ComFasterxmlVersionAccessors vaccForComFasterxmlVersionAccessors = new ComFasterxmlVersionAccessors(providers, config);
        private final ComGoogleVersionAccessors vaccForComGoogleVersionAccessors = new ComGoogleVersionAccessors(providers, config);
        private final ComSunVersionAccessors vaccForComSunVersionAccessors = new ComSunVersionAccessors(providers, config);
        public ComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.cloudinary</b>
         */
        public ComCloudinaryVersionAccessors getCloudinary() {
            return vaccForComCloudinaryVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.fasterxml</b>
         */
        public ComFasterxmlVersionAccessors getFasterxml() {
            return vaccForComFasterxmlVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.google</b>
         */
        public ComGoogleVersionAccessors getGoogle() {
            return vaccForComGoogleVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.sun</b>
         */
        public ComSunVersionAccessors getSun() {
            return vaccForComSunVersionAccessors;
        }

    }

    public static class ComCloudinaryVersionAccessors extends VersionFactory  {

        private final ComCloudinaryCloudinaryVersionAccessors vaccForComCloudinaryCloudinaryVersionAccessors = new ComCloudinaryCloudinaryVersionAccessors(providers, config);
        public ComCloudinaryVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.cloudinary.cloudinary</b>
         */
        public ComCloudinaryCloudinaryVersionAccessors getCloudinary() {
            return vaccForComCloudinaryCloudinaryVersionAccessors;
        }

    }

    public static class ComCloudinaryCloudinaryVersionAccessors extends VersionFactory  {

        public ComCloudinaryCloudinaryVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.cloudinary.cloudinary.http44</b> with value <b>1.36.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHttp44() { return getVersion("com.cloudinary.cloudinary.http44"); }

    }

    public static class ComFasterxmlVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonVersionAccessors vaccForComFasterxmlJacksonVersionAccessors = new ComFasterxmlJacksonVersionAccessors(providers, config);
        public ComFasterxmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.fasterxml.jackson</b>
         */
        public ComFasterxmlJacksonVersionAccessors getJackson() {
            return vaccForComFasterxmlJacksonVersionAccessors;
        }

    }

    public static class ComFasterxmlJacksonVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonCoreVersionAccessors vaccForComFasterxmlJacksonCoreVersionAccessors = new ComFasterxmlJacksonCoreVersionAccessors(providers, config);
        public ComFasterxmlJacksonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.fasterxml.jackson.core</b>
         */
        public ComFasterxmlJacksonCoreVersionAccessors getCore() {
            return vaccForComFasterxmlJacksonCoreVersionAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonCoreJacksonVersionAccessors vaccForComFasterxmlJacksonCoreJacksonVersionAccessors = new ComFasterxmlJacksonCoreJacksonVersionAccessors(providers, config);
        public ComFasterxmlJacksonCoreVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.fasterxml.jackson.core.jackson</b>
         */
        public ComFasterxmlJacksonCoreJacksonVersionAccessors getJackson() {
            return vaccForComFasterxmlJacksonCoreJacksonVersionAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreJacksonVersionAccessors extends VersionFactory  {

        public ComFasterxmlJacksonCoreJacksonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.fasterxml.jackson.core.jackson.databind</b> with value <b>2.17.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDatabind() { return getVersion("com.fasterxml.jackson.core.jackson.databind"); }

    }

    public static class ComGoogleVersionAccessors extends VersionFactory  {

        private final ComGoogleCodeVersionAccessors vaccForComGoogleCodeVersionAccessors = new ComGoogleCodeVersionAccessors(providers, config);
        public ComGoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.google.code</b>
         */
        public ComGoogleCodeVersionAccessors getCode() {
            return vaccForComGoogleCodeVersionAccessors;
        }

    }

    public static class ComGoogleCodeVersionAccessors extends VersionFactory  {

        private final ComGoogleCodeGsonVersionAccessors vaccForComGoogleCodeGsonVersionAccessors = new ComGoogleCodeGsonVersionAccessors(providers, config);
        public ComGoogleCodeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.google.code.gson</b>
         */
        public ComGoogleCodeGsonVersionAccessors getGson() {
            return vaccForComGoogleCodeGsonVersionAccessors;
        }

    }

    public static class ComGoogleCodeGsonVersionAccessors extends VersionFactory  {

        public ComGoogleCodeGsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.google.code.gson.gson</b> with value <b>2.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGson() { return getVersion("com.google.code.gson.gson"); }

    }

    public static class ComSunVersionAccessors extends VersionFactory  {

        private final ComSunMailVersionAccessors vaccForComSunMailVersionAccessors = new ComSunMailVersionAccessors(providers, config);
        public ComSunVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.sun.mail</b>
         */
        public ComSunMailVersionAccessors getMail() {
            return vaccForComSunMailVersionAccessors;
        }

    }

    public static class ComSunMailVersionAccessors extends VersionFactory  {

        private final ComSunMailJakartaVersionAccessors vaccForComSunMailJakartaVersionAccessors = new ComSunMailJakartaVersionAccessors(providers, config);
        public ComSunMailVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.sun.mail.jakarta</b>
         */
        public ComSunMailJakartaVersionAccessors getJakarta() {
            return vaccForComSunMailJakartaVersionAccessors;
        }

    }

    public static class ComSunMailJakartaVersionAccessors extends VersionFactory  {

        public ComSunMailJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.sun.mail.jakarta.mail</b> with value <b>2.0.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMail() { return getVersion("com.sun.mail.jakarta.mail"); }

    }

    public static class JakartaVersionAccessors extends VersionFactory  {

        private final JakartaServletVersionAccessors vaccForJakartaServletVersionAccessors = new JakartaServletVersionAccessors(providers, config);
        public JakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet</b>
         */
        public JakartaServletVersionAccessors getServlet() {
            return vaccForJakartaServletVersionAccessors;
        }

    }

    public static class JakartaServletVersionAccessors extends VersionFactory  {

        private final JakartaServletJakartaVersionAccessors vaccForJakartaServletJakartaVersionAccessors = new JakartaServletJakartaVersionAccessors(providers, config);
        private final JakartaServletJspVersionAccessors vaccForJakartaServletJspVersionAccessors = new JakartaServletJspVersionAccessors(providers, config);
        public JakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jakarta</b>
         */
        public JakartaServletJakartaVersionAccessors getJakarta() {
            return vaccForJakartaServletJakartaVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jsp</b>
         */
        public JakartaServletJspVersionAccessors getJsp() {
            return vaccForJakartaServletJspVersionAccessors;
        }

    }

    public static class JakartaServletJakartaVersionAccessors extends VersionFactory  {

        private final JakartaServletJakartaServletVersionAccessors vaccForJakartaServletJakartaServletVersionAccessors = new JakartaServletJakartaServletVersionAccessors(providers, config);
        public JakartaServletJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jakarta.servlet</b>
         */
        public JakartaServletJakartaServletVersionAccessors getServlet() {
            return vaccForJakartaServletJakartaServletVersionAccessors;
        }

    }

    public static class JakartaServletJakartaServletVersionAccessors extends VersionFactory  {

        public JakartaServletJakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>jakarta.servlet.jakarta.servlet.api</b> with value <b>6.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("jakarta.servlet.jakarta.servlet.api"); }

    }

    public static class JakartaServletJspVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlVersionAccessors vaccForJakartaServletJspJstlVersionAccessors = new JakartaServletJspJstlVersionAccessors(providers, config);
        public JakartaServletJspVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jsp.jstl</b>
         */
        public JakartaServletJspJstlVersionAccessors getJstl() {
            return vaccForJakartaServletJspJstlVersionAccessors;
        }

    }

    public static class JakartaServletJspJstlVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaVersionAccessors vaccForJakartaServletJspJstlJakartaVersionAccessors = new JakartaServletJspJstlJakartaVersionAccessors(providers, config);
        public JakartaServletJspJstlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jsp.jstl.jakarta</b>
         */
        public JakartaServletJspJstlJakartaVersionAccessors getJakarta() {
            return vaccForJakartaServletJspJstlJakartaVersionAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaServletVersionAccessors vaccForJakartaServletJspJstlJakartaServletVersionAccessors = new JakartaServletJspJstlJakartaServletVersionAccessors(providers, config);
        public JakartaServletJspJstlJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jsp.jstl.jakarta.servlet</b>
         */
        public JakartaServletJspJstlJakartaServletVersionAccessors getServlet() {
            return vaccForJakartaServletJspJstlJakartaServletVersionAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaServletVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaServletJspVersionAccessors vaccForJakartaServletJspJstlJakartaServletJspVersionAccessors = new JakartaServletJspJstlJakartaServletJspVersionAccessors(providers, config);
        public JakartaServletJspJstlJakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jsp.jstl.jakarta.servlet.jsp</b>
         */
        public JakartaServletJspJstlJakartaServletJspVersionAccessors getJsp() {
            return vaccForJakartaServletJspJstlJakartaServletJspVersionAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaServletJspVersionAccessors extends VersionFactory  {

        private final JakartaServletJspJstlJakartaServletJspJstlVersionAccessors vaccForJakartaServletJspJstlJakartaServletJspJstlVersionAccessors = new JakartaServletJspJstlJakartaServletJspJstlVersionAccessors(providers, config);
        public JakartaServletJspJstlJakartaServletJspVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl</b>
         */
        public JakartaServletJspJstlJakartaServletJspJstlVersionAccessors getJstl() {
            return vaccForJakartaServletJspJstlJakartaServletJspJstlVersionAccessors;
        }

    }

    public static class JakartaServletJspJstlJakartaServletJspJstlVersionAccessors extends VersionFactory  {

        public JakartaServletJspJstlJakartaServletJspJstlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api</b> with value <b>3.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("jakarta.servlet.jsp.jstl.jakarta.servlet.jsp.jstl.api"); }

    }

    public static class MysqlVersionAccessors extends VersionFactory  {

        private final MysqlMysqlVersionAccessors vaccForMysqlMysqlVersionAccessors = new MysqlMysqlVersionAccessors(providers, config);
        public MysqlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.mysql.mysql</b>
         */
        public MysqlMysqlVersionAccessors getMysql() {
            return vaccForMysqlMysqlVersionAccessors;
        }

    }

    public static class MysqlMysqlVersionAccessors extends VersionFactory  {

        private final MysqlMysqlConnectorVersionAccessors vaccForMysqlMysqlConnectorVersionAccessors = new MysqlMysqlConnectorVersionAccessors(providers, config);
        public MysqlMysqlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.mysql.mysql.connector</b>
         */
        public MysqlMysqlConnectorVersionAccessors getConnector() {
            return vaccForMysqlMysqlConnectorVersionAccessors;
        }

    }

    public static class MysqlMysqlConnectorVersionAccessors extends VersionFactory  {

        public MysqlMysqlConnectorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>mysql.mysql.connector.java</b> with value <b>8.0.33</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJava() { return getVersion("mysql.mysql.connector.java"); }

    }

    public static class OrgVersionAccessors extends VersionFactory  {

        private final OrgGlassfishVersionAccessors vaccForOrgGlassfishVersionAccessors = new OrgGlassfishVersionAccessors(providers, config);
        private final OrgJdbiVersionAccessors vaccForOrgJdbiVersionAccessors = new OrgJdbiVersionAccessors(providers, config);
        private final OrgJsonVersionAccessors vaccForOrgJsonVersionAccessors = new OrgJsonVersionAccessors(providers, config);
        private final OrgJunitVersionAccessors vaccForOrgJunitVersionAccessors = new OrgJunitVersionAccessors(providers, config);
        private final OrgMindrotVersionAccessors vaccForOrgMindrotVersionAccessors = new OrgMindrotVersionAccessors(providers, config);
        private final OrgSlf4jVersionAccessors vaccForOrgSlf4jVersionAccessors = new OrgSlf4jVersionAccessors(providers, config);
        public OrgVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish</b>
         */
        public OrgGlassfishVersionAccessors getGlassfish() {
            return vaccForOrgGlassfishVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jdbi</b>
         */
        public OrgJdbiVersionAccessors getJdbi() {
            return vaccForOrgJdbiVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.json</b>
         */
        public OrgJsonVersionAccessors getJson() {
            return vaccForOrgJsonVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.junit</b>
         */
        public OrgJunitVersionAccessors getJunit() {
            return vaccForOrgJunitVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.mindrot</b>
         */
        public OrgMindrotVersionAccessors getMindrot() {
            return vaccForOrgMindrotVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.slf4j</b>
         */
        public OrgSlf4jVersionAccessors getSlf4j() {
            return vaccForOrgSlf4jVersionAccessors;
        }

    }

    public static class OrgGlassfishVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebVersionAccessors vaccForOrgGlassfishWebVersionAccessors = new OrgGlassfishWebVersionAccessors(providers, config);
        public OrgGlassfishVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish.web</b>
         */
        public OrgGlassfishWebVersionAccessors getWeb() {
            return vaccForOrgGlassfishWebVersionAccessors;
        }

    }

    public static class OrgGlassfishWebVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebJakartaVersionAccessors vaccForOrgGlassfishWebJakartaVersionAccessors = new OrgGlassfishWebJakartaVersionAccessors(providers, config);
        public OrgGlassfishWebVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish.web.jakarta</b>
         */
        public OrgGlassfishWebJakartaVersionAccessors getJakarta() {
            return vaccForOrgGlassfishWebJakartaVersionAccessors;
        }

    }

    public static class OrgGlassfishWebJakartaVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebJakartaServletVersionAccessors vaccForOrgGlassfishWebJakartaServletVersionAccessors = new OrgGlassfishWebJakartaServletVersionAccessors(providers, config);
        public OrgGlassfishWebJakartaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish.web.jakarta.servlet</b>
         */
        public OrgGlassfishWebJakartaServletVersionAccessors getServlet() {
            return vaccForOrgGlassfishWebJakartaServletVersionAccessors;
        }

    }

    public static class OrgGlassfishWebJakartaServletVersionAccessors extends VersionFactory  {

        private final OrgGlassfishWebJakartaServletJspVersionAccessors vaccForOrgGlassfishWebJakartaServletJspVersionAccessors = new OrgGlassfishWebJakartaServletJspVersionAccessors(providers, config);
        public OrgGlassfishWebJakartaServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish.web.jakarta.servlet.jsp</b>
         */
        public OrgGlassfishWebJakartaServletJspVersionAccessors getJsp() {
            return vaccForOrgGlassfishWebJakartaServletJspVersionAccessors;
        }

    }

    public static class OrgGlassfishWebJakartaServletJspVersionAccessors extends VersionFactory  {

        public OrgGlassfishWebJakartaServletJspVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.glassfish.web.jakarta.servlet.jsp.jstl</b> with value <b>3.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJstl() { return getVersion("org.glassfish.web.jakarta.servlet.jsp.jstl"); }

    }

    public static class OrgJdbiVersionAccessors extends VersionFactory  {

        private final OrgJdbiJdbi3VersionAccessors vaccForOrgJdbiJdbi3VersionAccessors = new OrgJdbiJdbi3VersionAccessors(providers, config);
        public OrgJdbiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jdbi.jdbi3</b>
         */
        public OrgJdbiJdbi3VersionAccessors getJdbi3() {
            return vaccForOrgJdbiJdbi3VersionAccessors;
        }

    }

    public static class OrgJdbiJdbi3VersionAccessors extends VersionFactory  {

        public OrgJdbiJdbi3VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jdbi.jdbi3.core</b> with value <b>3.41.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.jdbi.jdbi3.core"); }

        /**
         * Version alias <b>org.jdbi.jdbi3.sqlobject</b> with value <b>3.43.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSqlobject() { return getVersion("org.jdbi.jdbi3.sqlobject"); }

    }

    public static class OrgJsonVersionAccessors extends VersionFactory  {

        public OrgJsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.json.json</b> with value <b>20240303</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJson() { return getVersion("org.json.json"); }

    }

    public static class OrgJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterVersionAccessors vaccForOrgJunitJupiterVersionAccessors = new OrgJunitJupiterVersionAccessors(providers, config);
        public OrgJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.junit.jupiter</b>
         */
        public OrgJunitJupiterVersionAccessors getJupiter() {
            return vaccForOrgJunitJupiterVersionAccessors;
        }

    }

    public static class OrgJunitJupiterVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitVersionAccessors vaccForOrgJunitJupiterJunitVersionAccessors = new OrgJunitJupiterJunitVersionAccessors(providers, config);
        public OrgJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.junit.jupiter.junit</b>
         */
        public OrgJunitJupiterJunitVersionAccessors getJunit() {
            return vaccForOrgJunitJupiterJunitVersionAccessors;
        }

    }

    public static class OrgJunitJupiterJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitJupiterVersionAccessors vaccForOrgJunitJupiterJunitJupiterVersionAccessors = new OrgJunitJupiterJunitJupiterVersionAccessors(providers, config);
        public OrgJunitJupiterJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.junit.jupiter.junit.jupiter</b>
         */
        public OrgJunitJupiterJunitJupiterVersionAccessors getJupiter() {
            return vaccForOrgJunitJupiterJunitJupiterVersionAccessors;
        }

    }

    public static class OrgJunitJupiterJunitJupiterVersionAccessors extends VersionFactory  {

        public OrgJunitJupiterJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.junit.jupiter.junit.jupiter.api</b> with value <b>5.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("org.junit.jupiter.junit.jupiter.api"); }

        /**
         * Version alias <b>org.junit.jupiter.junit.jupiter.engine</b> with value <b>5.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getEngine() { return getVersion("org.junit.jupiter.junit.jupiter.engine"); }

    }

    public static class OrgMindrotVersionAccessors extends VersionFactory  {

        public OrgMindrotVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.mindrot.jbcrypt</b> with value <b>0.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJbcrypt() { return getVersion("org.mindrot.jbcrypt"); }

    }

    public static class OrgSlf4jVersionAccessors extends VersionFactory  {

        private final OrgSlf4jSlf4jVersionAccessors vaccForOrgSlf4jSlf4jVersionAccessors = new OrgSlf4jSlf4jVersionAccessors(providers, config);
        public OrgSlf4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.slf4j.slf4j</b>
         */
        public OrgSlf4jSlf4jVersionAccessors getSlf4j() {
            return vaccForOrgSlf4jSlf4jVersionAccessors;
        }

    }

    public static class OrgSlf4jSlf4jVersionAccessors extends VersionFactory  {

        public OrgSlf4jSlf4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.slf4j.slf4j.simple</b> with value <b>2.0.9</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSimple() { return getVersion("org.slf4j.slf4j.simple"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
