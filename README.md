# CKEditor Integration with XWiki

Adds support for editing wiki pages using [CKEditor](http://ckeditor.com/).

Starting with XWiki 8.2 this is the default WYSIWYG content editor. On older versions of XWiki this application extends the Edit menu with a new entry called 'CKEditor' that loads a new edit mode where you can edit the content of the wiki page using the CKEditor.

* Project Lead: [Marius Dumitru Florea](http://www.xwiki.org/xwiki/bin/view/XWiki/mflorea)
* [Documentation & Download](http://extensions.xwiki.org/xwiki/bin/view/Extension/CKEditor+Integration)
* [Issue Tracker](http://jira.xwiki.org/browse/CKEDITOR)
* Communication: [Mailing List](http://dev.xwiki.org/xwiki/bin/view/Community/MailingLists>), [IRC]( http://dev.xwiki.org/xwiki/bin/view/Community/IRC)
* [Development Practices](http://dev.xwiki.org)
* Minimal XWiki version supported: XWiki 6.2.5
* License: LGPL 2.1+
* [Translations](http://l10n.xwiki.org/xwiki/bin/view/Contrib/CKEditorIntegration)
* Sonar Dashboard: N/A
* Continuous Integration Status: [![Build Status](http://ci.xwiki.org/job/XWiki%20Contrib/job/application-ckeditor/job/master/badge/icon)](http://ci.xwiki.org/view/Contrib/job/XWiki%20Contrib/job/application-ckeditor/job/master/)

## Building

You need Maven 3.1+ in order to build this extension.

## Release Steps

    ## Release the new version in JIRA and create the next version.

    ## Update the translations.
    ## * download the translation pack from l10n and unpack
    ##   http://l10n.xwiki.org/xwiki/bin/view/L10NCode/GetTranslationFile?name=Contrib.CKEditorIntegration&app=Contrib
    ## * copy the translation pages to the UI module sources
    ## * apply XAR format and review the changes
    ## * commit only the significant changes

    ## Prepare the tag for the new version.
    mvn org.apache.maven.plugins:maven-release-plugin:2.5:prepare -DautoVersionSubmodules -DskipTests -Darguments="-DskipTests" -Pintegration-tests

    ## Perform the release
    ## We skip the enforcer because the functional test modules have a recent parent that requires the latest Java while
    ## the actual code has an older parent (in order to support older versions of XWiki) that requires an older version
    ## of Java. Fortunately, we can release with the latest Java because ATM we don't have Java code outside the
    ## functional test modules.
    mvn org.apache.maven.plugins:maven-release-plugin:2.5:perform -DskipTests -Darguments="-DskipTests -Dxwiki.enforcer.skip=true" -Pintegration-tests

    ## Finish the release on http://nexus.xwiki.org (Staging Repositories)

    ## Update the documentation page on http://extensions.xwiki.org
    ## Keep the release notes (the list of JIRA issues) only for the 2 most recent releases.

    ## Announce the release

    ## Update the version used in XWiki Enterprise
