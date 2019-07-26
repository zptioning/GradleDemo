package com.zptioning.lib_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class LifeCyclePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("${project.name}")
        println "&&*&*&*&*&*&*&*&*   LifeCycle plugin entrance  *&*&*&*&*&*&*&*&*&"
    }
}