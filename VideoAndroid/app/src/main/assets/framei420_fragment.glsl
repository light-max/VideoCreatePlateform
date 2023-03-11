#version 300 es
precision mediump float;
in vec2 texCoord;
uniform sampler2D yTexture;
uniform sampler2D uTexture;
uniform sampler2D vTexture;
out vec4 fragment;
void main() {
    vec3 rgb;
    float y = texture(yTexture, texCoord).r;
    float u = texture(uTexture, texCoord).r-0.5;
    float v = texture(vTexture, texCoord).r-0.5;
    rgb.r = y + 1.13983*v;
    rgb.g = y - 0.39465*u - 0.58060*v;
    rgb.b = y + 2.03211*u;
    fragment = vec4(rgb, 1.);
}
