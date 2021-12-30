#ifdef GL_ES
precision mediump float;
#endif

#define FACTOR 3.0
#define RADIUS 0.1

varying vec2 v_texCoord;

uniform vec2 u_touch;
uniform sampler2D u_temperature;

void main(){
    float x = v_texCoord.x;
    float y = v_texCoord.y;

    float splash = smoothstep(u_touch.x - RADIUS, u_touch.x + RADIUS, x)
                 * smoothstep(u_touch.x + RADIUS, u_touch.x - RADIUS, x)
                 * smoothstep(u_touch.y - RADIUS, u_touch.y + RADIUS, y)
                 * smoothstep(u_touch.y + RADIUS, u_touch.y - RADIUS, y);
    splash *= FACTOR;

    float temperature = texture2D(u_temperature, v_texCoord).x;
    temperature = clamp(temperature + splash, 0.0, 1.1);

    gl_FragColor = vec4(temperature, 0.0, 0.0, 1.0);
}
