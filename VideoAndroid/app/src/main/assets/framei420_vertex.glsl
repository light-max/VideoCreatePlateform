#version 300 es
layout(location = 0) in vec2 vCoord;
layout(location = 1) in vec2 tCoord;
uniform mat4 uMatrix;
out vec2 texCoord;
void main() {
    gl_Position = uMatrix * vec4(vCoord, 0., 1.);
    texCoord = tCoord;
}
