import turtle
from random import randrange
turtle.up()
turtle.hideturtle()
turtle.goto(0,-350)
turtle.down()
turtle.tracer(0)
#turtle.speed(0)

axiom = 'X'
temp = ''
trans={'+':'+','-':'-','F':'F','X':'F[@[-X]+X]','[':'[',']':']','@':'@'}
stek=[]
angle =lambda : randrange(0,45)
step=90
color=[0.35,0.2,0.0]
thick=10

for k in range(15):
    for i in axiom:
        temp+=trans[i]
    axiom=temp
    temp=''
print(axiom)

turtle.left(90)
turtle.pensize(thick)
for i in axiom:

    if i=='+':
        turtle.right(angle())
    elif i == '-':
        turtle.left(angle())

    elif i == '[':
        ang,pos=turtle.heading(),turtle.pos()
        stek.append((ang,pos,thick,step,color[1]))
    elif i==']':
        ang,pos,thick,step,color[1]=stek.pop()
        turtle.pensize(thick)
        turtle.setheading(ang)
        turtle.up()
        turtle.goto(pos)
        turtle.down()
    elif i=='@':
        step-=6
        color[1]+=0.04
        thick-=1
        thick=max(1,thick)
        turtle.pensize(thick)


    elif i=='F' or i=='X':
        turtle.forward(step)



turtle.mainloop()
