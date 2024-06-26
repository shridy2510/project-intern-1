PGDMP                      |            postgres    16.2    16.2                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    5    postgres    DATABASE     j   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';
    DROP DATABASE postgres;
                postgres    false                       0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                   postgres    false    3607                        3079    16384 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false                       0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2            �            1259    17468    user information    TABLE       CREATE TABLE public."user information" (
    "user-id" bigint NOT NULL,
    "first-name" character varying(255),
    gmail character varying(255),
    "last-name" character varying(255),
    password character varying(255),
    username character varying(255)
);
 &   DROP TABLE public."user information";
       public         heap    postgres    false            �            1259    17467    user information_user-id_seq    SEQUENCE     �   CREATE SEQUENCE public."user information_user-id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public."user information_user-id_seq";
       public          postgres    false    217                       0    0    user information_user-id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public."user information_user-id_seq" OWNED BY public."user information"."user-id";
          public          postgres    false    216            �            1259    17771    users    TABLE     �   CREATE TABLE public.users (
    id bigint NOT NULL,
    "first-name" character varying(255),
    gmail character varying(255),
    "last-name" character varying(255),
    "user-id" character varying(255),
    username character varying(255)
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    17770    users_id_seq    SEQUENCE     u   CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public          postgres    false    219                       0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public          postgres    false    218            y           2604    17471    user information user-id    DEFAULT     �   ALTER TABLE ONLY public."user information" ALTER COLUMN "user-id" SET DEFAULT nextval('public."user information_user-id_seq"'::regclass);
 K   ALTER TABLE public."user information" ALTER COLUMN "user-id" DROP DEFAULT;
       public          postgres    false    216    217    217            z           2604    17774    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    218    219    219                      0    17468    user information 
   TABLE DATA           m   COPY public."user information" ("user-id", "first-name", gmail, "last-name", password, username) FROM stdin;
    public          postgres    false    217   "                 0    17771    users 
   TABLE DATA           Z   COPY public.users (id, "first-name", gmail, "last-name", "user-id", username) FROM stdin;
    public          postgres    false    219   j                  0    0    user information_user-id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public."user information_user-id_seq"', 3, true);
          public          postgres    false    216                       0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 2, true);
          public          postgres    false    218            |           2606    17475 &   user information user information_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public."user information"
    ADD CONSTRAINT "user information_pkey" PRIMARY KEY ("user-id");
 T   ALTER TABLE ONLY public."user information" DROP CONSTRAINT "user information_pkey";
       public            postgres    false    217            ~           2606    17778    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    219               8   x�3��(�����K/�L̓�83��ґ%�|.c���<�DIFb&������ {�         l   x�5�1�0k�/b$���_h�XIDE��13����YNW��Ǐ�����6����{G˝�S��x�J �����Lg�oF�-cފaQj�u��N4�[�U�� _��)1     