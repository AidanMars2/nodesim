#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 vertexColor;
out vec4 color;

uniform vec2 scale;

void main() {
    vec2 scaledLocation = position * scale;
    gl_Position = vec4(scaledLocation, 0.0f, 1.0f);
    color = vertexColor;
}