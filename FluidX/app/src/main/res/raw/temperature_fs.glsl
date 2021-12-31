#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform float u_timestamp;
uniform float u_conductivity;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_dx2;
uniform float u_dy2;
uniform sampler2D u_temperature;

void main(){
    vec2 top = vec2(v_texCoord.x, v_texCoord.y + u_heightTexel);
    vec2 bot = vec2(v_texCoord.x, v_texCoord.y - u_heightTexel);
    vec2 left = vec2(v_texCoord.x - u_widthTexel, v_texCoord.y);
    vec2 right = vec2(v_texCoord.x + u_widthTexel, v_texCoord.y);

    float xT = texture2D(u_temperature, top).x;
    float xB = texture2D(u_temperature, bot).x;
    float xL = texture2D(u_temperature, left).x;
    float xR = texture2D(u_temperature, right).x;
    
    float dx = u_conductivity * u_timestamp * u_dx2;
    float dy = u_conductivity * u_timestamp * u_dy2;

    float xC = texture2D(u_temperature, v_texCoord).x;
    float xNew = xC + dx * (xR - 2.0 * xC + xL) + dy * (xT - 2.0 * xC + xB);
    xNew -= 0.001;
    xNew = max(xNew, 0.0);

    gl_FragColor = vec4(xNew, 0.0, 0.0, 1.0);
}
