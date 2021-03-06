/*
 * Copyright 2020 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.flightrecorderstarter.trigger;

import de.mirkosertic.flightrecorderstarter.core.StartRecordingCommand;

public class Trigger {

    private String expression;
    private StartRecordingCommand startRecordingCommand;

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(final String expression) {
        this.expression = expression;
    }

    public StartRecordingCommand getStartRecordingCommand() {
        return this.startRecordingCommand;
    }

    public void setStartRecordingCommand(final StartRecordingCommand startRecordingCommand) {
        this.startRecordingCommand = startRecordingCommand;
    }
}
