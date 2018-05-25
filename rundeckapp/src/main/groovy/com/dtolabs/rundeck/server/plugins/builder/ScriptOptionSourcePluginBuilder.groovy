/*
 * Copyright 2018 Rundeck, Inc. (http://rundeck.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtolabs.rundeck.server.plugins.builder

import com.dtolabs.rundeck.plugins.option.OptionSourcePlugin
import com.dtolabs.rundeck.server.plugins.services.PluginBuilder
import org.codehaus.groovy.runtime.InvokerHelper


class ScriptOptionSourcePluginBuilder extends ScriptPluginBuilder implements PluginBuilder<OptionSourcePlugin> {
    Map<String, Closure> handlers
    /**
     * Create a builder for a plugin with the specified plugin/provider name.
     * @param clazz @param name the name of the plugin/provider
     */
    ScriptOptionSourcePluginBuilder(final Class clazz, final String name) {
        super(clazz, name)
    }

    @Override
    OptionSourcePlugin buildPlugin() {
        return new ScriptOptionSourcePlugin(handlers,descriptionBuilder.build())
    }

    @Override
    Object invokeMethod(final String name, final Object args) {
        List list = InvokerHelper.asList(args);
        if(list.size() == 1 && list[0] instanceof Closure) {
            if(name.equals("getOptionMap")) {
                handlers["getOptionMap"] = list[0]
            }
            return true
        }
        throw new MissingMethodException(name.toString(), getClass(), list.toArray(), false);
    }
}
