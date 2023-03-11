#version 300 es
precision mediump float;
in vec2 texCoord;
uniform sampler2D uTexture;
out vec4 fragment;
void main() {
    fragment = texture(uTexture, texCoord);
}
