package org.Core.Component;

import static org.lwjgl.opengl.GL11.GL_FALSE;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
;
public class Shade {
    private int vertexShader;
    private int fragmentShader;
    private int shaderProgram;
    int success[];
    private CharSequence vertexShadeSc = 
    "#version 330 core\n"+
    "layout (location = 0) in vec3 aPos; \n" +
    "layout (location = 1) in vec3 aColor;\r\n" + //
    "layout (location = 2) in vec2 InTexCoord;"+
    "out vec3 ourColor;\r\n" + //
    "out vec2 TexCoord;"+
    
    "uniform mat4 transform;\n" +
   " uniform mat4 model; " +
    "uniform mat4 view;" +
    "uniform mat4 projection;" +


    "void main() \n" +
    "{\n"+
   
    "gl_Position = projection * view * transform  * vec4(aPos, 1.0);\n" +
    "ourColor = aColor;"+ 


    "TexCoord = InTexCoord;" +
    "}";

    private CharSequence fragmentShadeSc = 
            "#version 330 core\n" +
            "out vec4 FragColor;\r\n" + //

           
            "in vec3 ourColor;\r\n" + //
            "in vec2 TexCoord;\r\n" + //
            
            "uniform sampler2D ourTexture;\r\n" + //
            "uniform sampler2D texture2;"+
          
            "void main()\n" +
            "{\n" +
            "if(texture(ourTexture, TexCoord).a < 0.1){discard;}"+
        
            "vec4 vertexColor =mix(texture(ourTexture, TexCoord), texture(texture2, TexCoord),0.0);"+
            "gl_FragColor = vertexColor;\n" +
            "}\n";
    public Shade()
    {
        
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        shaderProgram = glCreateProgram();


    }
    public int getFragmentShader() {
        return fragmentShader;
    }
    public int getVertexShader() {
        return vertexShader;
    }
    public void compileDefautl()
    {
        compileShade(fragmentShadeSc, fragmentShader);
        compileShade(vertexShadeSc, vertexShader);
    }
    public void compileShade(CharSequence source, int Shade)
    {
        glShaderSource(Shade, source);
        glCompileShader(Shade);

        success = new int[1];
        glGetShaderiv(Shade, GL_COMPILE_STATUS, success);
   


       glAttachShader(shaderProgram, Shade);

       if (glGetShaderi(Shade, GL_COMPILE_STATUS) == GL_FALSE)
       {
           System.out.println("Error: " + glGetShaderInfoLog(Shade));
       }

       
    }
    public void LinkShade()
    {
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        
    }
    public int getShaderProgram() {
        return shaderProgram;
    }
    public void Bind()
    {
        glUseProgram(shaderProgram);
    }
}
