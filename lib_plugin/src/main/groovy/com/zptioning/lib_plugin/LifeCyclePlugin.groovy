package com.zptioning.lib_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class LifeCyclePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("${project.name}")
        println "&&*&*&*&*&*&*&*&*   LifeCycle plugin entrance  *&*&*&*&*&*&*&*&*&"

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new LifeCycleTransform(project))
    }
}