import pygame
from random import randrange

size=800
sight=50

x,y=randrange(0,size,sight),randrange(0,size,sight) #kordinati zmei
apple=randrange(0,size,sight),randrange(0,size,sight) #kordinati apple

lenth=1
snake=[(x,y)]
dx,dy=0,0 #napravlenie dvizhenia
speed=5
fps=speed


pygame.init()
window=pygame.display.set_mode([size,size])
clock=pygame.time.Clock()

while True:
    window.fill(pygame.color('black'))
    #draw snake
    [(pygame.draw.rect(window,pygame.color('green'),(i,j,sight,sight)) )for i,j in snake]
    #draw apple
    pygame.draw.rect (window, pygame.color ('red'), (*apple, sight, sight))

    pygame.display.flip()
    clock.tick(fps)

    for event in pygame.event.get():
        if event.type ==pygame.QUIT:
            exit()
