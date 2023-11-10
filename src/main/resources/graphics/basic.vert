#version 330 core

in vec2 position;
in vec4 vertexColor;
out vec4 color;

uniform vec2 scale;

void main() {
    gl_Position = vec4(position * scale, 0.0f, 1.0f);
    color = vertexColor;
}