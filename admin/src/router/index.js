import { createRouter, createWebHashHistory } from 'vue-router';
import { useUserStore } from '../store/user';

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue'), meta: { public: true } },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/Dashboard.vue') },
      { path: 'audit', name: 'Audit', component: () => import('../views/audit/Audit.vue') },
      { path: 'users', name: 'Users', component: () => import('../views/users/Users.vue') },
      { path: 'houses', name: 'Houses', component: () => import('../views/houses/Houses.vue') },
      { path: 'complaints', name: 'Complaints', component: () => import('../views/complaints/Complaints.vue') },
      { path: 'settings', name: 'Settings', component: () => import('../views/settings/Settings.vue') }
    ]
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const store = useUserStore();
  if (!to.meta.public && !store.isLogin) {
    next({ path: '/login' });
  } else {
    next();
  }
});

export default router;
