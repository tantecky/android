#ifdef GL_ES
precision mediump float;
#endif

// increase SCALE to mark a neighbour texel
// 0.5 is one texel
#define SCALE 0.5

varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform vec2 u_touch;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_time;
uniform sampler2D u_temperature;

void main(){
    float temperature = texture2D(u_temperature, v_texCoord).x;

    float xCenter = v_texCoord.x;
    float yCenter = v_texCoord.y;

    float dx = abs(xCenter - u_touch.x);
    float dy = abs(yCenter - u_touch.y);

    if (dx < SCALE * u_widthTexel && dy < SCALE * u_heightTexel) {
        temperature = 1.0f;
    }

    gl_FragColor = vec4(temperature, 0.0, 0.0, 1.0);
}
