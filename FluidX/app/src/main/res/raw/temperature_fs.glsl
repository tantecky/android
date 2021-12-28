#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_time;
uniform sampler2D u_temperature;


void main(){
    float xCenter = v_texCoord.x;
    float yCenter = v_texCoord.y;

    vec2 center = vec2(xCenter, yCenter);
    vec2 top = vec2(xCenter, yCenter + u_heightTexel);
    vec2 bot = vec2(xCenter, yCenter - u_heightTexel);
    vec2 left = vec2(xCenter - u_widthTexel, yCenter);
    vec2 right = vec2(xCenter + u_widthTexel, yCenter);

    // float temperature = fract(texture2D(u_temperature, v_texCoord).x + 0.005);
    float temperature = 0.0;

    // is boundary texel
    if (top.y > 1.0 || bot.y < 0.0 || right.x > 1.0 || left.x < 0.0) {
        temperature = 1.0;
    }

    //gl_FragColor = vec4(temperature, 0.0, 0.0, 1.0);
    gl_FragColor = vec4(temperature, 0.0, 0.0, 1.0);
}
