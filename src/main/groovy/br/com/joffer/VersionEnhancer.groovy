package com.jhood

import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionEnhancer implements Plugin<Project> {
    void apply(Project project) {
		project.extensions.add("VersionEnhancer", MyPluginExtension)

		
		def versionPropsFile = file('version.properties')
		def versionBuild

		if (versionPropsFile.canRead()) {
			def Properties versionProps = new Properties()
			versionProps.load(new FileInputStream(versionPropsFile))
			versionBuild = versionProps['VERSION_BUILD'].toInteger()
		} else {
			versionBuild = 0
		}

		ext.autoIncrementBuildNumber = {
			if (versionPropsFile.canRead()) {
				def Properties versionProps = new Properties()
				versionProps.load(new FileInputStream(versionPropsFile))
				versionBuild = versionProps['VERSION_BUILD'].toInteger() + 1
				versionProps['VERSION_BUILD'] = versionBuild.toString()
				versionProps.store(versionPropsFile.newWriter(), null)
			} else {
				versionBuild = 0
			}
		}
		
		gradle.taskGraph.whenReady { taskGraph ->
			autoIncrementBuildNumber()    
		}
    }
}
